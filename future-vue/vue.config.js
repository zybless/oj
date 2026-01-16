const UglifyJsPlugin = require("uglifyjs-webpack-plugin"); // 清除注释
const CompressionWebpackPlugin = require("compression-webpack-plugin"); // 开启压缩

// 是否为生产环境
const isProduction = process.env.NODE_ENV === "production";

// 本地环境是否需要使用cdn
const devNeedCdn = false;

module.exports = {
  publicPath: isProduction ? "https://static.oicourse.com/project-v2/" : "/",
  assetsDir: "assets",
  devServer: {
    open: true, // npm run serve后自动打开页面
    host: "0.0.0.0", // 匹配本机IP地址(默认是0.0.0.0)
    port: 8066, // 开发服务器运行端口号
    proxy: {
      "/api": {
        //   以'/api'开头的请求会被代理进行转发
        target: "http://49.234.155.58:6688", 
        // target: "http://localhost:6688", 
        changeOrigin: true,
      },
    },
    disableHostCheck: true,
  },
  //去除生产环境的productionSourceMap
  productionSourceMap: false,

  chainWebpack: (config) => {
    // 只有在生产环境才开启分析器，不然本地开发每次都弹网页很烦
    if (isProduction) {
      config
        .plugin("webpack-bundle-analyzer")
        .use(require("webpack-bundle-analyzer").BundleAnalyzerPlugin);
    }
  },
  configureWebpack: (config) => {
    if (isProduction) {
      config.mode = "production";

      config["performance"] = {
        //打包文件大小配置
        maxEntrypointSize: 10000000,
        maxAssetSize: 30000000,
      };
      config.plugins.push(
        new UglifyJsPlugin({
          uglifyOptions: {
            output: {
              comments: false, // 去掉注释
            },
            warnings: false,
            compress: {
              drop_console: false,
              drop_debugger: false,
            },
          },
        })
      );
      // 服务器也要相应开启gzip
      config.plugins.push(
        new CompressionWebpackPlugin({
          filename: "[path].gz[query]",
          algorithm: "gzip",
          test: /\.(js|css)$/, // 匹配文件名
          threshold: 10000, // 对超过10k的数据压缩
          deleteOriginalAssets: false, // 不删除源文件
          minRatio: 0.8, // 压缩比
        })
      );
    }
  },
};
