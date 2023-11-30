package com.example.couriersystem

import java.io.Serializable

class EachOrder(Id:String,Location:String,AddresseeId:String,AddresseeName:String,AddresseePhone:String,CourierId:String):
    Serializable {
    var Id=Id
    var Location=Location
    var AddresseeId=AddresseeId
    var AddresseeName=AddresseeName
    var AddresseePhone=AddresseePhone
    var CourierId=CourierId
}