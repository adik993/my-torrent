const webpack = require('webpack');
const webpackMerge = require('webpack-merge');
const path = require('path');
const options = require('./base.js');

const API_URL = 'http://localhost:8080/';

module.exports = function (env) {
    return webpackMerge(options(), {
        devtool: "cheap-eval-source-map",
        devServer: {
            contentBase: path.resolve(__dirname, '../dist'),
            compress: true,
            port: 9090,
            proxy: {
                '/api': {
                    target: API_URL,
                    // pathRewrite: {'^/api': ''} //If api doesn't start with /api
                }
            }
        },
        plugins: [
            new webpack.HotModuleReplacementPlugin(),
            new webpack.DefinePlugin({
                'process.env': {
                    'NODE_ENV': JSON.stringify(env)
                }
            }),
        ]
    });
};