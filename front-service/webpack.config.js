const HtmlWebpackPlugin = require("html-webpack-plugin");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const path = require("path");
const { DefinePlugin } = require("webpack");

module.exports = {
  entry: "./src/index.tsx",
  resolve: {
    extensions: [".js", ".jsx", ".ts", ".tsx"],
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: ["ts-loader"],
        exclude: /node_modules/,
      },
      {
        test: /\.(png|jpe?g|gif)$/,
        use: [
          {
            loader: "file-loader",
          },
        ],
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"],
      },
    ],
  },
  output: {
    path: path.join(__dirname, "/build"),
    filename: "bundle.js",
    publicPath: "/",
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "./public/index.html",
    }),
    new CleanWebpackPlugin(),
    new DefinePlugin({
      "process.env": JSON.stringify(process.env),
    }),
  ],
  devServer: {
    port: 3000,
    open: true,
    proxy: {
      '/api/*': {
        target: 'http://3.36.96.40:8000', // 실제 서버 주소
        pathRewrite: { '/api': '' }, // proxy path 를 제거하도록 다시 쓴다
        changeOrigin: true,
      },
    },
  },
};