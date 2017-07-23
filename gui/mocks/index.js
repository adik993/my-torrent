const express = require('express');
const app = express();
const providersJson = require('./json/providers.json');
const searchFlashS01E01Json = require('./json/search-flash-s01e01.json');
const searchFlashS01E02Json = require('./json/search-flash-s01e02.json');

app.get('/api/user', function (req, resp) {
    resp.send(JSON.stringify({name: 'John', surname: 'Doe'}));
});

app.get('/api/providers', function (req, resp) {
    resp.send(providersJson);
});

app.get('/api/search', function (req, resp) {
    if ('Flash S01E01' === req.query.query) {
        resp.send(searchFlashS01E01Json)
    } else if ('Flash S01E02' === req.query.query) {
        resp.send(searchFlashS01E02Json)
    } else {
        resp.send([])
    }
});

const PORT = 8080;

app.listen(PORT, function () {
    console.log(`Example app listening on port ${PORT}!`);
});