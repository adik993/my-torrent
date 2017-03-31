const express = require('express');
const app = express();

app.get('/api/user', function (req, resp) {
    resp.send(JSON.stringify({name: 'John', surname: 'Doe'}));
});

app.listen(5000, function () {
    console.log('Example app listening on port 5000!');
});