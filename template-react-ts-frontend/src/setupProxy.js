const { createProxyMiddleware } = require('http-proxy-middleware')

module.exports = function(app) {
    console.log("AAA");
    app.use(
        createProxyMiddleware('/api', {
            target: 'http://localhost:8180',
            changeOrigin: true,
            pathRewrite: {
                '^/api': ''
            }
        })
    )
}