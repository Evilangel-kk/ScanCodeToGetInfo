const ws = require("nodejs-websocket"); //引入依赖包
const POST = 8080; //定义端口

var mysql = require("mysql");
var connection = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "123456",
    database: "user"
});

connection.connect((error) => {
    if (error) {
        console.log("连接失败" + error);
    } else {
        console.log("连接数据库成功");
    }
});

function closeConnection() {
    connection.end();
}

// 创建一个server
const server = ws.createServer((connect) => {
    //每次只要有新的用户加入，就会为当前用户创建一个connect对象，同时调用一下这个回调函数。
    console.log("New connection");
    connect.sendText("连接成功!!!");

    // text事件：接收用户请求，得到用户传输进来的数据。
    connect.on("text", (data) => {
        // 每当接受到用户请求事件，这个回调函数就会被触发。
        console.log("Received " + data);

        // sendText/send 方法：接受到请求后，响应一个数据给用户。因为是connect调用，所以只给当前connet对应的用户发送，如果需要给所有用户发送（广播），需要connections这个数组
    });

    // 连接断开 触发close事件
    connect.on("close", (code, reason) => {
        console.log("connection closed");
        code && console.log(code);
        reason && console.log(reason);
    });

    // error：监听服务异常事件，放置因报错而断掉整个服务。
    connect.on("error", () => {
        // 如果触发了close事件，就会走error异常事件(刷新也会)，所以必须加error
        console.log("连接出现异常");
    });
});
server.listen(POST, () => {
    console.log("webSocket服务启动成功了,监听了端口" + POST);
});