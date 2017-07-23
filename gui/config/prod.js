const webpack = require('webpack');
const webpackMerge = require('webpack-merge');
const path = require('path');
// const FaviconsWebpackPlugin = require('favicons-webpack-plugin');
const options = require('./base');

module.exports = function () {
    return webpackMerge(options(), {
        output: {
            path: path.join(__dirname, '../build', 'dist'),
            sourceMapFilename: "[name].map"
        },
        plugins: [
            new webpack.LoaderOptionsPlugin({
                minimize: true,
                debug: false
            }),
            new webpack.optimize.UglifyJsPlugin({
                beautify: false,
                mangle: {
                    screw_ie8: true,
                    keep_fnames: true
                },
                compress: {
                    screw_ie8: true
                },
                comments: false
            }),
            new webpack.DefinePlugin({
                'process.env': {
                    'NODE_ENV': JSON.stringify("production")
                }
            })
            // new FaviconsWebpackPlugin('images/favicon.png')
        ]
    });
};