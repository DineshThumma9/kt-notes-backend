package com.example.demo.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class HashPass {

    private val bCrypt = BCryptPasswordEncoder()

    fun encode(raw:String) : String = bCrypt.encode(raw)
    fun matches(raw: String,hash:String) : Boolean = bCrypt.matches(raw,hash)




}