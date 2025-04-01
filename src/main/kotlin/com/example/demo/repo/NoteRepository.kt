package com.example.demo.repo

import com.example.demo.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


interface NoteRepository : MongoRepository<Note, ObjectId> {


    fun findByOwnerId(ownerId:ObjectId) : List<Note>



}