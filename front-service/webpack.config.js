const path = require('path');
const {DefinePlugin} = require('webpack');
const dotenv = require('dotenv');

dotenv.config();

module.exports = {
  mode: 'development',
  entry: './src/index.tsx',
  output: {
    publicPath: '/',
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'dist'),
  },
  resolve: {
    extensions: ['.ts', '.tsx', '.js', '.jsx'],
  },
  module: {
    rules: [
      {
        test: /\.(ts|js)x?$/,
        exclude: /node_modules/,
        use: {
          loader: 'ts-loader',
        },
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  plugins: [new DefinePlugin({
    "process.env": JSON.stringify(process.env),
  })],
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
    historyApiFallback: true, // 404 오류 시 /index.html로 리다이렉트
  },
};