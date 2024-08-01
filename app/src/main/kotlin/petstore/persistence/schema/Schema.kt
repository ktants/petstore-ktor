package petstore.persistence.schema

import petstore.persistence.PetGender
import petstore.persistence.PetOwner
import petstore.persistence.Pet
import petstore.persistence.PetPhoto
import petstore.persistence.Photo
import petstore.persistence.Speciality
import petstore.persistence.PetType
import petstore.persistence.VetPhoto
import petstore.persistence.VetSpecialty
import petstore.persistence.PetVisit


private val tables =
    setOf(
        PetGender.Table,
        PetOwner.Table,
        Pet.Table,
        PetPhoto.Table,
        Photo.Table,
        Speciality.Table,
        PetType.Table,
        VetPhoto.Table,
        VetSpecialty.Table,
        PetVisit.Table
    )
