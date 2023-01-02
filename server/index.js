const express = require("express")
const req = require("express/lib/request")
const multer = require('multer')
const { db } = require('./fb.js')
const path = require('path')


const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'images')
    },
    filename: (req, file, cb) => {
        console.log(file)
        cb(null, Date.now() + path.extname(file.originalname))
    }
})

const upload = multer({ storage: storage })
const app = express()

app.post('/add', upload.single('image'), (req, res) => {
    console.log(req)
    db.collection('pics').add({ picid: 1 })
    res.status(200).send({ "message": req.file.filename.split('.')[0] })
})

app.get('/pic/:file', (req, res) => {
    var file = req.params.file + ".png"
    res.status(200).sendFile(__dirname + "/images/" + file)
})

app.listen(3000)