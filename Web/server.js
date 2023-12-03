const ws = require("nodejs-websocket"); //引入依赖包
const PORT = 8080; //定义端口

// 用于存储连接的映射表
const clients = new Map();

//定义返回状态信息
const LOGIN_SUCCESS = "1";
const LOGIN_FAIL = "2";
const ENROLL_SUCCESS = "3";
const ENROLL_FAIL = "4";
const GETINFO_SUCCESS = "5";
const GETINFO_FAIL = "6";
const ID_EXISTED = "7";
const ID_INEXIST = "8";
const ORDERID_EXISTED = "9"
const ADDORDER_SUCCESS = "10"
const COURIER_GET_ORDERS_SUCCESS = "11"
const COURIER_GET_ORDERS_FAIL = "12"
const ADDRESSEE_GET_ORDERS_SUCCESS = "13"
const ADDRESSEE_GET_ORDERS_FAIL = "14"
const NOTFUND_PHONE = "15"
const FUND_PHONE = "16"
const FUND_NAME = "17"
const NOTFUND_NAME = "18"


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

// 向特定连接发送消息的函数
function sendToClient(clientId, message) {
    const client = clients.get(clientId);
    if (client) {
        client.send(message);
    } else {
        console.log('Client not found');
    }
}

var num = 0;

// 创建一个server
const server = ws.createServer((connect) => {
    //每次只要有新的用户加入，就会为当前用户创建一个connect对象，同时调用一下这个回调函数。
    console.log("New connection");
    connect.sendText("连接成功!!!");
    const UserAgent = "" + connect.headers['user-agent'];
    if (UserAgent.includes("Windows") || UserAgent.includes("Mac") || UserAgent.includes("Linux")) {
        // 生成唯一标识符
        var clientId = "ManagerService";
        num++;
        if (num > 1) {
            clientId += num;
            console.log("又一个服务台连接上来了");
        }

        // 将连接加入映射表
        clients.set(clientId, connect);
    }
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
                            connect.sendText("Courier:" + result[0].Id + ":" + result[0].Name + ":" + result[0].Phone);
                            connection.query("update courier set State=1 where Id=?", receivedMsg[1], (err, result) => {
                                if (err) {
                                    console.log("更新状态出错" + err);
                                    connect.send("更新状态出错");
                                } else {
                                    if (result[0] == null) {
                                        console.log("更新状态成功");
                                        sendToClient("ManagerService" + num, 'UPDATE_STATE');
                                    }
                                }
                            });
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
                            connection.query("update addressee set State=1 where Id=?", receivedMsg[1], (err, result) => {
                                if (err) {
                                    console.log("更新状态出错" + err);
                                    connect.send("更新状态出错");
                                } else {
                                    if (result[0] == null) {
                                        console.log("更新状态成功");
                                        sendToClient("ManagerService" + num, 'UPDATE_STATE');
                                    }
                                }
                            });
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
                        connection.query("insert into courier values (?,?,?,?,?)", [receivedMsg[1], receivedMsg[2], receivedMsg[3], receivedMsg[4], 0], (err, result) => {
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
                        connection.query("insert into addressee values (?,?,?,?,?)", [receivedMsg[1], receivedMsg[2], receivedMsg[3], receivedMsg[4], 0], (err, result) => {
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
        } else if (receivedMsg[0] == "AddOrder") {
            connection.query("select * from orders where ID=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    if (result[0] != null) {
                        console.log("已存在该订单号");
                        connect.sendText(ORDERID_EXISTED);
                    } else {
                        console.log("添加成功");
                        connect.sendText(ADDORDER_SUCCESS);
                        connection.query("insert into orders values (?,?,?,?,?,?)", [receivedMsg[1], receivedMsg[2], receivedMsg[3], receivedMsg[4], receivedMsg[5], null], (err, result) => {
                            if (err) {
                                console.log("添加出错" + err);
                                connect.send("添加出错");
                            } else {
                                console.log("添加成功");
                                connect.send("添加成功");
                            }
                        })
                    }
                }
            })
        } else if (receivedMsg[0] == "courierAskAllOrders") {
            connection.query("select * from orders where CourierId=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    if (result[0] == null) {
                        console.log("未查找到订单");
                        connect.sendText(COURIER_GET_ORDERS_FAIL);
                    } else {
                        var text = "courierOrders&"
                        var i = 0;
                        while (result[i]) {
                            text += (result[i].Id + ":" + result[i].Location + ":" + result[i].AddresseeId + ":" + result[i].AddresseeName + ":" + result[i].AddresseePhone + ":" + result[i].CourierId);
                            text += "&";
                            i++;
                        }
                        text = text.slice(0, -1);
                        connect.sendText(text);
                    }
                }
            })
        } else if (receivedMsg[0] == "addresseeAskAllOrders") {
            connection.query("select * from orders where AddresseeId=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    if (result[0] == null) {
                        console.log("未查找到订单");
                        connect.sendText(ADDRESSEE_GET_ORDERS_FAIL);
                    } else {
                        var text = "addresseeOrders&"
                        var i = 0;
                        while (result[i]) {
                            text += (result[i].Id + ":" + result[i].Location + ":" + result[i].AddresseeId + ":" + result[i].AddresseeName + ":" + result[i].AddresseePhone + ":" + result[i].CourierId);
                            text += "&";
                            i++;
                        }
                        text = text.slice(0, -1);
                        connect.sendText(text);
                    }
                }
            })
        } else if (receivedMsg[0] == "FindCourierPhoneAndName") {
            connection.query("select * from courier where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    if (result[0] == null) {
                        console.log("未查找到电话号");
                        connect.sendText(NOTFUND_PHONE);
                    } else {
                        var text = "courierPhoneAndName:" + result[0].Phone + ":" + result[0].Name;
                        console.log("查找到电话号:" + result[0].Phone);
                        console.log("查找到姓名:" + result[0].Name);
                        connect.sendText(text);
                    }
                }
            })
        } else if (receivedMsg[0] == "ManagerLogin") {
            console.log(receivedMsg[0]);
            connection.query("select * from manager where Name=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("查询出错" + err);
                    connect.send("查询出错");
                } else {
                    if (result[0] == null) {
                        console.log("未查找到管理员");
                        connect.sendText("NOTFUND_MANAGER");
                    } else {
                        if (result[0].Password == receivedMsg[2]) {
                            console.log("查找到管理员");
                            connect.sendText("FUND_MANAGER");
                        }
                    }
                }
            })
        } else if (receivedMsg[0] == "SearchCourier") {
            if (receivedMsg[1] != "null") {
                connection.query("select * from courier where Name like ?", [`%${receivedMsg[1]}%`], (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到快递员");
                            connect.sendText("NOTFUND_COURIER");
                        } else {
                            console.log("查找到快递员");
                            var text = "FUND_COURIER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Password}:${result[i].Name}:${result[i].Phone}:${result[i].State}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            } else {
                connection.query("select * from courier", null, (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到快递员");
                            connect.sendText("NOTFUND_COURIER");
                        } else {
                            console.log("查找到快递员");
                            var text = "FUND_COURIER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Password}:${result[i].Name}:${result[i].Phone}:${result[i].State}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            }
        } else if (receivedMsg[0] == "courierChange") {
            if (receivedMsg[2] == receivedMsg[1]) {
                connection.query("update courier set Id=?,Password=?,`Name`=?,Phone=? where Id=?", [receivedMsg[2], receivedMsg[3], receivedMsg[4], receivedMsg[5], receivedMsg[1]], (err, result) => {
                    if (err) {
                        console.log("更新出错" + err);
                        connect.send("更新出错");
                    } else {
                        if (result[0] == null) {
                            //更新成功UPDATE_SUCCESS
                            console.log("更新成功");
                            connect.sendText("UPDATE_SUCCESS");
                        } else {
                            console.log("更新失败" + result[0]);
                            connect.send("更新失败");
                        }
                    }
                })
            } else {
                connection.query("select * from courier where Id=?", receivedMsg[2], (err, res) => {
                    if (err) {
                        console.log("更新出错" + err);
                        connect.send("更新出错");
                    } else {
                        console.log(res[0]);
                        if (res[0] == null) {
                            //新账号不冲突
                            connection.query("update courier set Id=?,Password=?,`Name`=?,Phone=? where Id=?", [receivedMsg[2], receivedMsg[3], receivedMsg[4], receivedMsg[5], receivedMsg[1]], (err, result) => {
                                if (err) {
                                    console.log("更新出错" + err);
                                    connect.send("更新出错");
                                } else {
                                    if (result[0] == null) {
                                        //更新成功UPDATE_SUCCESS
                                        console.log("更新成功");
                                        connect.sendText("UPDATE_SUCCESS");
                                    } else {
                                        console.log("更新失败" + result[0]);
                                        connect.send("更新失败");
                                    }
                                }
                            })
                        } else {
                            console.log("新账号已存在");
                            connect.send(ID_EXISTED);
                        }
                    }
                })
            }

        } else if (receivedMsg[0] == "courierDelete") {
            connection.query("delete from courier where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("删除出错" + err);
                    connect.send("删除出错");
                } else {
                    if (result[0] == null) {
                        console.log("删除成功");
                        connect.sendText("DELETE_COURIER_SUCCESS");
                    } else {
                        console.log("删除失败");
                        connect.sendText("DELETE_COURIER_FAIL");
                    }
                }
            });
            connection.query("update orders set CourierId='' where CourierId=?", (err, result) => {
                if (err) {
                    console.log("重置出错" + err);
                    connect.send("重置出错");
                } else {
                    if (result[0] == null) {
                        console.log("重置成功");
                    } else {
                        console.log("重置失败");
                    }
                }
            });
        } else if (receivedMsg[0] == "SearchAddressee") {
            if (receivedMsg[1] != "null") {
                connection.query("select * from addressee where Name like ?", [`%${receivedMsg[1]}%`], (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到收件人");
                            connect.sendText("NOTFUND_ADDRESSEE");
                        } else {
                            console.log("查找到收件人");
                            var text = "FUND_ADDRESSEE";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Password}:${result[i].Name}:${result[i].Phone}:${result[i].State}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            } else {
                connection.query("select * from addressee", null, (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到收件人");
                            connect.sendText("NOTFUND_ADDRESSEE");
                        } else {
                            console.log("查找到收件人");
                            var text = "FUND_ADDRESSEE";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Password}:${result[i].Name}:${result[i].Phone}:${result[i].State}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            }
        } else if (receivedMsg[0] == "addresseeChange") {
            if (receivedMsg[2] == receivedMsg[1]) {
                connection.query("update addressee set Id=?,Password=?,`Name`=?,Phone=? where Id=?", [receivedMsg[2], receivedMsg[3], receivedMsg[4], receivedMsg[5], receivedMsg[1]], (err, result) => {
                    if (err) {
                        console.log("更新出错" + err);
                        connect.send("更新出错");
                    } else {
                        if (result[0] == null) {
                            //更新成功UPDATE_SUCCESS
                            console.log("更新成功");
                            connect.sendText("UPDATE_SUCCESS");
                        } else {
                            console.log("更新失败" + result[0]);
                            connect.send("更新失败");
                        }
                    }
                })
            } else {
                connection.query("select * from addressee where Id=?", receivedMsg[2], (err, res) => {
                    if (err) {
                        console.log("更新出错" + err);
                        connect.send("更新出错");
                    } else {
                        console.log(res[0]);
                        if (res[0] == null) {
                            //新账号不冲突
                            connection.query("update addressee set Id=?,Password=?,`Name`=?,Phone=? where Id=?", [receivedMsg[2], receivedMsg[3], receivedMsg[4], receivedMsg[5], receivedMsg[1]], (err, result) => {
                                if (err) {
                                    console.log("更新出错" + err);
                                    connect.send("更新出错");
                                } else {
                                    if (result[0] == null) {
                                        //更新成功UPDATE_SUCCESS
                                        console.log("更新成功");
                                        connect.sendText("UPDATE_SUCCESS");
                                    } else {
                                        console.log("更新失败" + result[0]);
                                        connect.send("更新失败");
                                    }
                                }
                            })
                        } else {
                            console.log("新账号已存在");
                            connect.send(ID_EXISTED);
                        }
                    }
                })
            }

        } else if (receivedMsg[0] == "addresseeDelete") {
            connection.query("delete from addressee where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("删除出错" + err);
                    connect.send("删除出错");
                } else {
                    if (result[0] == null) {
                        console.log("删除成功");
                        connect.sendText("DELETE_ADDRESSEE_SUCCESS");
                    } else {
                        console.log("删除失败");
                        connect.sendText("DELETE_ADDRESSEE_FAIL");
                    }
                }
            });
            connection.query("delete from orders where AddresseeId=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("删除出错" + err);
                    connect.send("删除出错");
                } else {
                    if (result[0] == null) {
                        console.log("删除成功");
                    } else {
                        console.log("删除失败");
                    }
                }
            });
        } else if (receivedMsg[0] == "SearchAssignedOrder") {
            if (receivedMsg[1] != "null") {
                connection.query("select * from orders where CourierId != '' and (Location like ? or CourierId like ? or AddresseeName like ?)", [`%${receivedMsg[1]}%`, `%${receivedMsg[1]}%`, `%${receivedMsg[1]}%`], (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到订单");
                            connect.sendText("NOTFUND_ASSGINED_ORDER");
                        } else {
                            console.log("查找到订单");
                            var text = "FUND_ASSGINED_ORDER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Location}:${result[i].AddresseeName}:${result[i].CourierId}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            } else {
                connection.query("select * from orders where CourierId != ''", null, (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到订单");
                            connect.sendText("NOTFUND_ASSGINED_ORDER");
                        } else {
                            console.log("查找到订单");
                            var text = "FUND_ASSGINED_ORDER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Location}:${result[i].AddresseeName}:${result[i].CourierId}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            }
        } else if (receivedMsg[0] == "SearchNotAssignedOrder") {
            if (receivedMsg[1] != "null") {
                connection.query("select * from orders where (CourierId = '' or courierId is NULL) and (Location like ? or AddresseeName like ?)", [`%${receivedMsg[1]}%`, `%${receivedMsg[1]}%`], (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到订单");
                            connect.sendText("NOTFUND_NOTASSGINED_ORDER");
                        } else {
                            console.log("查找到订单");
                            var text = "FUND_NOTASSGINED_ORDER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Location}:${result[i].AddresseeName}:${result[i].AddresseePhone}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            } else {
                connection.query("select * from orders where CourierId = '' or CourierId is NULL", null, (err, result) => {
                    if (err) {
                        console.log("查询出错" + err);
                        connect.send("查询出错");
                    } else {
                        if (result[0] == null) {
                            console.log("未查找到订单");
                            connect.sendText("NOTFUND_NOTASSGINED_ORDER");
                        } else {
                            console.log("查找到订单");
                            var text = "FUND_NOTASSGINED_ORDER";
                            var i = 0;
                            while (result[i]) {
                                var t = `&${result[i].Id}:${result[i].Location}:${result[i].AddresseeName}:${result[i].AddresseePhone}`
                                text += t;
                                i++;
                            }
                            connect.sendText(text);
                        }
                    }
                })
            }
        } else if (receivedMsg[0] == "OrderChange") {
            connection.query("update orders set Location=?,CourierId=? where Id=?", [receivedMsg[2], receivedMsg[3], receivedMsg[1]], (err, result) => {
                if (err) {
                    console.log("更新出错" + err);
                    connect.send("更新出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新成功");
                        connect.sendText("UPDATE_ORDER_SUCCESS");
                    } else {
                        console.log("更新失败" + result[0]);
                        connect.send("更新失败");
                    }
                }
            });
            connection.query("select * from orders where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("更新出错" + err);
                    connect.send("更新出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新失败");
                    } else {
                        console.log("更新成功");
                        generateQRCodeAndSave(result[0]);
                    }
                }
            });
        } else if (receivedMsg[0] == "AssignCourier") {
            connection.query("update orders set CourierId=? where Id=?", [receivedMsg[2], receivedMsg[1]], (err, result) => {
                if (err) {
                    console.log("更新出错" + err);
                    connect.send("更新出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新成功");
                        connect.sendText("UPDATE_ORDER_SUCCESS");
                    } else {
                        console.log("更新失败" + result[0]);
                        connect.send("更新失败");
                    }
                }
            });
            connection.query("select * from orders where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("更新出错" + err);
                    connect.send("更新出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新失败");
                    } else {
                        console.log("更新成功");
                        generateQRCodeAndSave(result[0]);
                    }
                }
            });
        } else if (receivedMsg[0] == "GetNumbers") {
            connection.query("select count(*) as num from orders", (err, result) => {
                if (err) {
                    console.log("统计出错" + err);
                    connect.send("统计出错");
                } else {
                    connect.sendText("numOfOrder:" + result[0].num);
                }
            });
            connection.query("select count(*) as num from courier", (err, result) => {
                if (err) {
                    console.log("统计出错" + err);
                    connect.send("统计出错");
                } else {
                    connect.sendText("numOfCourier:" + result[0].num);
                }
            });
            connection.query("select count(*) as num from addressee", (err, result) => {
                if (err) {
                    console.log("统计出错" + err);
                    connect.send("统计出错");
                } else {
                    connect.sendText("numOfAddressee:" + result[0].num);
                }
            });
        } else if (receivedMsg[0] == "GetCourierOrdersRadio") {
            connection.query("select CourierId, count(*) as num from orders group by CourierId", (err, result) => {
                if (err) {
                    console.log("聚类出错" + err);
                    connect.send("聚类出错");
                } else {
                    var text = "CourierOrdersRadio";
                    var i = 0;
                    while (result[i]) {
                        var t = "&" + result[i].CourierId + ":" + result[i].num;
                        text += t;
                        i++;
                    }
                    connect.sendText(text);
                }
            })
        } else if (receivedMsg[0] == "GetAddresseeOrdersRadio") {
            connection.query("select AddresseeId, AddresseeName, count(*) as num from orders group by AddresseeId, AddresseeName", (err, result) => {
                if (err) {
                    console.log("聚类出错" + err);
                    connect.send("聚类出错");
                } else {
                    var text = "AddresseeOrdersRadio";
                    var i = 0;
                    while (result[i]) {
                        var t = "&" + result[i].AddresseeName + ":" + result[i].num;
                        text += t;
                        i++;
                    }
                    connect.sendText(text);
                }
            })
        } else if (receivedMsg[0] == "COURIER_LEAVE") {
            connection.query("update courier set State=0 where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("更新状态出错" + err);
                    connect.send("更新状态出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新状态成功");
                        sendToClient("ManagerService" + num, "UPDATE_STATE");
                    }
                }
            })
        } else if (receivedMsg[0] == "ADDRESSEE_LEAVE") {
            connection.query("update addressee set State=0 where Id=?", receivedMsg[1], (err, result) => {
                if (err) {
                    console.log("更新状态出错" + err);
                    connect.send("更新状态出错");
                } else {
                    if (result[0] == null) {
                        console.log("更新状态成功");
                        sendToClient("ManagerService" + num, "UPDATE_STATE");
                    }
                }
            })
        } else if (receivedMsg[0] == "GetImage") {
            // 读取图片文件
            const imgData = fs.readFileSync(`../CodeImg/${receivedMsg[1]}.png`);

            // 发送图片数据给客户端
            connect.send(imgData);
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
function generateQRCodeAndSave(order) {
    console.log(order);
    const url = `码上识件:${order.Id}:${order.Location}:${order.AddresseeId}:${order.AddresseeName}:${order.AddresseePhone}:${order.CourierId}`; // 替换为你实际使用的 URL
    qr.toFile(
        `../CodeImg/${order.Id}.png`, // 保存路径
        url, { margin: 1, width: 256, height: 256 }, // 二维码配置
        (err) => {
            if (err) {
                console.log(err);
            } else {
                console.log(`二维码生成成功并保存到 ../CodeImg/${order.Id}.png`);
            }
        }
    );
}

// connection.query("select * from orders where CourierId != '' or CourierId is not NULL", (err, result) => {
//     var i = 0;
//     while (result[i]) {
//         console.log(result[i]);
//         if (result[i].CourierId != "") {
//             generateQRCodeAndSave(result[i]);
//         }
//         i++;
//     }
// });