<template>
    <div id="AddresseePage">
        <!-- 面包屑 -->
        <el-breadcrumb separator="/" style="font-size:20px;">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>收件人列表</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="page-content">
            <div style="display:flex;">
                <!-- 搜索框 -->
                <div class="search" style="width: 200px;margin:10px;">
                    <el-input placeholder="请输入关键词搜索" v-model="keyword" class="input-with-select">
                        <template #append>
                            <el-button slot="append" @click="searchByKeyword()">
                                <el-icon>
                                    <Search/>
                                </el-icon>
                            </el-button>
                        </template>
                        
                    </el-input>
                </div>
                <!-- 添加按钮 -->
                <el-button type="primary" style="margin:10px;" @click="addAddressee()">添加</el-button>
            </div>

            <!-- 表格 -->
            <el-table
                :data="list"
                stripe
                height="570"
                style="width: 100%">
                <el-table-column
                prop="id"
                label="账号">
                </el-table-column>
                <el-table-column
                prop="password"
                label="密码">
                </el-table-column>
                <el-table-column
                prop="name"
                label="昵称">
                </el-table-column>
                <el-table-column
                prop="phone"
                label="联系方式">
                </el-table-column>
                <el-table-column
                prop="state"
                label="状态">
                    <template #default="scope">
                        <el-switch v-model="scope.row.state"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button
                        type="primary"
                        @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                        type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-dialog title="添加收件人" v-model="dialogFormVisible">
            <el-form :model="formData">
                <el-form-item label="账号" :label-width="formLabelWidth">
                    <el-input v-model="formData.id" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth">
                    <el-input v-model="formData.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="昵称" :label-width="formLabelWidth">
                    <el-input v-model="formData.nickname" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话号" :label-width="formLabelWidth">
                    <el-input v-model="formData.phone" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="addConfirm()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="修改收件人信息" v-model="dialogFormChangeVisible">
            <el-form :model="formDataChange">
                <el-form-item label="账号" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.id" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="昵称" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.nickname" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话号" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.phone" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormChangeVisible = false">取 消</el-button>
                <el-button type="primary" @click="changeConfirm()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import SocketService from '../components/websocket'
    export default{
        name:"AddresseeView",
        data(){
            return{
                socketServe: SocketService.Instance,
                keyword:null,
                list:[],
                formData:{
                    id:"",
                    password:"",
                    nickname:"",
                    phone:""
                },
                formDataChange:{
                    id:"",
                    password:"",
                    nickname:"",
                    phone:""
                },
                dialogFormVisible:false,
                dialogFormChangeVisible:false,
                oldId:"",
                newId:"",
            }
        },
        mounted(){
            this.socketServe=SocketService.Instance;
            this.socketServe.send("SearchAddressee:"+this.keyword);
            // 添加socket通知监听
	        window.addEventListener('onmessageWS', this.getSocketData)
        },
        methods:{
            getSocketData(res){
                console.log(res.detail.data);
                if(res.detail.data.includes("FUND_ADDRESSEE")){
                    var msg=res.detail.data.split("&");
                    console.log(msg.length);
                    for(var i=1;i<msg.length;i++){
                        var m=msg[i].split(":");
                        var item={
                            "id":m[0],
                            "password":m[1],
                            "name":m[2],
                            "phone":m[3],
                            "state":m[4]=="0"?false:true,
                        }
                        this.list.push(item);
                    }
                    console.table(this.list);
                }else if(res.detail.data.includes("NOTFUND_ADDRESSEE")){
                    //未查找到收件人
                }else if(res.detail.data=="7"){
                    //账号已经存在
                    this.$message.error('账号已经存在');
                }else if(res.detail.data=="3"){
                    //添加成功
                    this.$message({
                        message: '添加收件人成功',
                        type: 'success'
                    });
                    this.dialogFormVisible=false;
                    this.searchByKeyword();
                }else if(res.detail.data=="UPDATE_SUCCESS"){
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    this.dialogFormChangeVisible=false;
                    this.searchByKeyword();
                }else if(res.detail.data=="DELETE_ADDRESSEE_SUCCESS"){
                    this.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    this.searchByKeyword();
                }else if(res.detail.data=="DELETE_ADDRESSEE_FAIL"){
                    this.$message.error('删除失败');
                }else if(res.detail.data=="UPDATE_STATE"){
                    console.log("UPDATE_STATE");
                    this.searchByKeyword();
                }
            },
            handleEdit(index,row){
                this.dialogFormChangeVisible=true;
                this.oldId=row.id;
                this.formDataChange.id=row.id;
                this.formDataChange.password=row.password;
                this.formDataChange.nickname=row.name;
                this.formDataChange.phone=row.phone;
            },
            handleDelete(index,row){
                this.socketServe.send("addresseeDelete:"+row.id);
            },
            addAddressee(){
                this.formData.id="";
                this.formData.password="";
                this.formData.nickname="";
                this.formData.phone="";
                this.dialogFormVisible=true;
            },
            addConfirm(){
                this.socketServe.send("addresseeFindId:"+this.formData.id+":"+this.formData.password+":"+this.formData.nickname+":"+this.formData.phone);
            },
            changeConfirm(){
                this.socketServe.send("addresseeChange:"+this.oldId+":"+this.formDataChange.id+":"+this.formDataChange.password+":"+this.formDataChange.nickname+":"+this.formDataChange.phone);
            },
            searchByKeyword(){
                this.list=[];
                this.socketServe.send("SearchAddressee:"+this.keyword);
            },
        },
    }
</script>

<style scoped>
#AddresseePage{
    padding: 20px;
}
.page-content{
    background-color: white;
    margin-top: 20px;
    display: flex;
    flex-direction: column;
}
</style>