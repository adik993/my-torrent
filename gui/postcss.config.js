const autoprefixer = require('autoprefixer');

module.exports = {
    plugins: [
        autoprefixer({browsers: ['last 100 versions']})
    ]
};