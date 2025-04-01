package com.example.demo.repo

import com.example.demo.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository




interface UserRepositary : MongoRepository<User,ObjectId> {


    fun findByEmail(email:String) : User?


}