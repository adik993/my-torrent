function buildConfig(env) {
    return require('./config/' + env + '.js')(env)
}
console.log(JSON.stringify(buildConfig('dev')));

module.exports = buildConfig;
