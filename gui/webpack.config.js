function buildConfig(env) {
    const config = require('./config/' + env + '.js')(env);
    console.log(JSON.stringify(config));
    return config;
}

module.exports = buildConfig;
