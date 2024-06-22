"use strict"

const {Model, Schema} = require("mongoose")

const DOCUMENT_NAME = 'User'
const COLLECTION_NAME = 'Users'

var userSchema = new Schema({
    email: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    password: {
        type: String,
        required: true,
        trim: true
    },
    fullname: {
        type: String,
        required: true,
    },
    birthday: {
        type: Date,
        required: true
    },
    profileImageUrl: {
        type: String,
        default: ''
    },
    friends: [
        {
            type: Schema.Types.ObjectId,
            ref: 'User'
        }
    ]
}, {
    timestamps: true,
    collection: COLLECTION_NAME
})

module.exports = model(DOCUMENT_NAME, userSchema)