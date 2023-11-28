const ws = require("nodejs-websocket"); //引入依赖包
const PORT = 8080; //定义端口

//定义返回状态信息
const LOGIN_SUCCESS = "1";
const LOGIN_FAIL = "2";
const ENROLL_SUCCESS = "3";
const ENROLL_FAIL = "4";
const GETINFO_SUCCESS = "5";
const GETINFO_FAIL = "6";
const ID_EXISTED = "7";
const ID_INEXIST = "8";

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
        var receivedMsg = data.split(":");

        if (receivedMsg[0] == "courierFindPwdById") {
            //快递员账号查询对应密码
            connection.query("select * from courier where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    console.log(result[0]);
                    if (result[0] == null) {
                        console.log("账号不存在");
                        connect.sendText(LOGIN_FAIL);
                    } else {
                        if (result[0].Password == receivedMsg[2]) {
                            console.log("账号密码验证成功");
                            connect.sendText(LOGIN_SUCCESS);
                            generateQRCodeAndSave(result[0]);
                            connect.sendText("Courier:" + result[0].Id + ":" + result[0].Name + ":" + result[0].Phone);
                        } else {
                            console.log("账号密码验证失败");
                            connect.sendText(LOGIN_FAIL);
                        }
                    }
                }
            });
        } else if (receivedMsg[0] == "addresseeFindPwdById") {
            //收件人账号查询对应密码
            connection.query("select * from addressee where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    console.log(result[0]);
                    if (result[0] == null) {
                        console.log("账号不存在");
                        connect.sendText(LOGIN_FAIL);
                    } else {
                        if (result[0].Password == receivedMsg[2]) {
                            console.log("账号密码验证成功");
                            connect.sendText(LOGIN_SUCCESS);
                            connect.sendText("Addressee:" + result[0].Id + ":" + result[0].Name + ":" + result[0].Phone);
                        } else {
                            console.log("账号密码验证失败");
                            connect.sendText(LOGIN_FAIL);
                        }
                    }
                }
            });
        } else if (receivedMsg[0] == "courierFindId") {
            //快递员账号查询是否存在
            connection.query("select * from courier where ID=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    console.log(result[0]);
                    if (result[0] != null) {
                        console.log("已存在该账号");
                        connect.sendText(ID_EXISTED);
                    } else {
                        console.log("注册成功");
                        connect.sendText(ENROLL_SUCCESS);
                        connection.query("insert into courier values (?,?,?,?)", [receivedMsg[1], receivedMsg[2], receivedMsg[3], receivedMsg[4]], (err, result) => {
                            if (err) {
                                console.log("插入出错" + err);
                                connect.send("插入出错");
                            } else {
                                console.log("插入成功");
                                connect.send("插入成功");
                            }
                        });
                    }
                }
            });
        } else if (receivedMsg[0] == "addresseeFindId") {
            //收件人账号查询是否存在
            connection.query("select * from addressee where ID=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    console.log(result[0]);
                    if (result[0] != null) {
                        console.log("已存在该账号");
                        connect.sendText(ID_EXISTED);
                    } else {
                        console.log("注册成功");
                        connect.sendText(ENROLL_SUCCESS);
                        connection.query("insert into addressee values (?,?,?,?)", [receivedMsg[1], receivedMsg[2], receivedMsg[3], receivedMsg[4]], (err, result) => {
                            if (err) {
                                console.log("插入出错" + err);
                                connect.send("插入出错");
                            } else {
                                console.log("插入成功");
                                connect.send("插入成功");
                            }
                        });
                    }
                }
            });
        }
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
server.listen(PORT, () => {
    console.log("webSocket服务启动成功了,监听了端口" + PORT);
});

// 引入 qrcode 模块和 fs 模块
const qr = require("qrcode");
const fs = require("fs");

// 生成二维码并保存到本地
function generateQRCodeAndSave(user) {
    const url = `${user.Id}:${user.Password}:${user.Name}:${user.Phone}`; // 替换为你实际使用的 URL
    qr.toFile(
        `../CodeImg/${user.Id}.png`, // 保存路径
        url, { margin: 1, width: 256, height: 256 }, // 二维码配置
        (err) => {
            if (err) {
                console.log(err);
            } else {
                console.log(`二维码生成成功并保存到 ../CodeImg/${user.Id}.png`);
            }
        }
    );
}