package com.example.demo.security

import com.example.demo.model.RefreshToken
import com.example.demo.model.User
import com.example.demo.repo.RefreshTokenRepository
import com.example.demo.repo.UserRepositary

import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
class AuthService(

    private val jwtService: JwtService,
    private val userRepository : UserRepositary,
    private val hashEncoder: HashPass,
    private val refreshTokenRepository: RefreshTokenRepository

) {



    fun register(email:String,password:String): User {

        val user = userRepository.findByEmail(email.trim())
        if(user != null){
            throw ResponseStatusException(HttpStatus.CONFLICT , "A user already exists")
        }
        return userRepository.save(
            User(
                email = email,
                hash_password = hashEncoder.encode(password)
            )
        )
    }


    fun login(email: String,password: String):TokenPair {
        val user = userRepository.findByEmail(email)  ?:  throw BadCredentialsException("Invalid Creditionals")

        if(!hashEncoder.matches(password,user.hash_password)){
            throw BadCredentialsException("Invlaid credtionls")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())



        storeRefreshToken(user.id,newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )

    }

    @Transactional
    fun refresh(refreshToken:String):TokenPair{

        if(!jwtService.validateRefreshToken(refreshToken)){
            throw ResponseStatusException(HttpStatusCode.valueOf(401),"Invalid refresh Token")

        }


        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow{
            ResponseStatusException(HttpStatusCode.valueOf(404),"Invalidate refresh token")
        }



        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id,hashed)
            ?: throw
            ResponseStatusException(
                HttpStatusCode.valueOf(404),
                "Invalidate refresh token"
            )


        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id,hashed)

        val newAcessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id,newRefreshToken)

        return TokenPair(
            accessToken = newAcessToken,
            refreshToken = newRefreshToken
        )
    }

     private fun storeRefreshToken(userId:ObjectId,rawRefreshToken:String){
         val hashed = hashToken(rawRefreshToken)
         val expirys = jwtService.refreshTokenValidityMs
         val expiresAt = Instant.now().plusMillis(expirys)


         refreshTokenRepository.save(
            RefreshToken(
                userId=userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )

         )


     }





    private fun hashToken(token:String):String{
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

}

data class TokenPair(

    val accessToken: String,
    val refreshToken:String

)
