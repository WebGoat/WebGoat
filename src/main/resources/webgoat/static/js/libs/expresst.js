const express = require('express')
const router = express.Router()

router.get('/greeting', (req, res) => {
    const { name }  = req.query;
    res.send('<h1> Hello :'+ name +"</h1>")
})

router.get('/greet-template', (req,res) => {
    name = req.query.name
    res.render('index', { user_name: name});
})

module.exports = router
