const path = require('path');

module.exports = {
    // ... 다른 설정 ...

    module: {
        rules: [
            {
                test: /\.svg$/, // .svg 확장자를 가진 파일에 대해서
                use: ['file-loader'], // file-loader를 사용합니다.
            },
        ],
    },
};
