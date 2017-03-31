const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const WebpackProvideGlobalPlugin = require('webpack-provide-global-plugin');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = function () {
    return {
        context: path.resolve(__dirname, '../src'),
        entry: {
            app: './index.js'
        },
        output: {
            path: path.resolve(__dirname, '../dist'),
            publicPath: '/',
            filename: '[name].bundle.[hash].js'
        },
        resolve: {
            modules: [
                path.resolve('./src'),
                path.resolve('./node_modules')
            ]
        },
        module: {
            rules: [{
                test: /\.js$/,
                include: path.resolve(__dirname, '../src'),
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: [
                            ['es2015', {
                                modules: false
                            }],
                            'stage-0',
                            'react'
                        ]
                    }
                }]
            },
                {
                    test: /\.css$/,
                    use: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: ['css-loader', 'postcss-loader']
                    })
                },
                {
                    test: /\.scss$/,
                    use: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: ['css-loader', 'postcss-loader', 'sass-loader']
                    })
                },
                {
                    test: /\.(jpe?g|png|gif|svg|ico)$/i,
                    use: [
                        'url-loader?limit=10000',
                        'img-loader'
                    ]
                },
                {
                    test: /\.(woff2?|ttf|eot|svg)$/,
                    use: [
                        'url-loader?limit=10000'
                    ]
                },
                {
                    test: /bootstrap\/dist\/js\/umd\//,
                    use: [
                        'imports-loader?jQuery=jquery'
                    ]
                }]
        },
        plugins: [
            new HtmlWebpackPlugin({
                template: 'templates/index.ejs',
                favicon: 'images/favicon.ico',
                inject: 'body',
            }),
            new webpack.ProvidePlugin({
                jQuery: 'jquery',
                $: 'jquery',
                jquery: 'jquery'
            }),
            new WebpackProvideGlobalPlugin({
                "es6-promise": "Promise",
                "whatwg-fetch": "fetch"
            }),
            new ExtractTextPlugin('style.css')
        ]
    };
};
