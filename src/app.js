const compression = require("compression")
const express = require("express")
const helmet = require("helmet")
const morgan = require("morgan")
require("dotenv").config()
require("./dbs/mongoose.db")

const app = express()

app.use(morgan("combined"))
app.use(helmet())
app.use(compression())
app.use(express.json())
app.use(express.urlencoded({
    extended: true
}))

app.use((req, res, next) => {
    const error = new Error("Not found")
    error.status = 404
    next(error)
})

app.use((error, req, res, next) => {
    console.error(error)
    const statusCode = error.status || 500
    return res.status(statusCode).json({
        status: statusCode,
        message: error.message || "Internal server error"
    })
})

module.exports = app