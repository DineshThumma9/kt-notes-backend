package com.example.demo.controller

import com.example.demo.model.Note
import com.example.demo.repo.NoteRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.Instant




@Controller
@RequestMapping("/notes")
class NotesController(

    private val noteRepository: NoteRepository


) {




    data class NoteRequest(

        val id : String?,
        @field:NotBlank(message = "Title Cant be Blank")
        val title : String,
        val content: String,
        val color:  Long,
        val OwnerId : String?

    )


    data class NoteResponse(
        val id:String,
        val title: String,
        val content: String,
        val color: Long,
        val createdAt : Instant

    )



    @PostMapping
    fun save(
      @Valid @RequestBody body: NoteRequest
    ):NoteResponse{
        val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        val  note = noteRepository.save(
             Note(
                 id = body.id?.let { ObjectId(it) } ?: ObjectId.get(),
                 title = body.title,
                 content = body.content,
                 color =  body.color,
                 createdAt = Instant.now(),
                 ownerId = ObjectId(ownerId)
             )
        )
        return  note.toResponse()

    }


    @GetMapping
    fun findByOwnerId():List<NoteResponse>{
     val ownerId = SecurityContextHolder.getContext().authentication.principal as String
        return noteRepository.findByOwnerId(ObjectId(ownerId)).map {
             it.toResponse()
        }
    }



    private fun Note.toResponse() : NoteResponse{


        return NoteResponse(
            id = id.toHexString(),
            title =  title,
            content =  content,
            color = color,
            createdAt = createdAt

        )


    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteById(
        @PathVariable id:String

    ){

           val note = noteRepository.findById(ObjectId(id)).orElseThrow{
               IllegalArgumentException("Note not found")
           }

        val ownerId = SecurityContextHolder.getContext().authentication.principal as String

        if(note.ownerId.toHexString() == ownerId){
            noteRepository.deleteById(ObjectId(id))
        }

    }



}