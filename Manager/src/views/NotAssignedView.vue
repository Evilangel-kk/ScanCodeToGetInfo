<template>
    <div id="NotAssignedPage">
        <!-- 面包屑 -->
        <el-breadcrumb separator="/" style="font-size:20px;">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>未分配订单</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="page-content">
            <div style="display:flex;">
                <!-- 搜索框 -->
                <div class="search" style="width: 200px;margin:10px;">
                    <el-input placeholder="请输入关键词搜索" v-model="keyword" class="input-with-select">
                        <template #append>
                            <el-button slot="append" @click="searchNotAssignedOrderByKeyword()">
                                <el-icon>
                                    <Search/>
                                </el-icon>
                            </el-button>
                        </template>
                        
                    </el-input>
                </div>
            </div>

            <!-- 表格 -->
            <el-table
                :data="list"
                stripe
                height="550"
                style="width: 100%">
                <el-table-column
                prop="id"
                label="订单编号">
                </el-table-column>
                <el-table-column
                prop="location"
                label="配送地址">
                </el-table-column>
                <el-table-column
                prop="addressee"
                label="收件人">
                </el-table-column>
                <el-table-column
                prop="phone"
                label="联系电话">
                </el-table-column>
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button
                        type="primary"
                        @click="handleEdit(scope.$index, scope.row)">分配</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog title="分配订单" v-model="dialogFormChangeVisible">
            <el-form :model="formDataChange">
                <el-select v-model="formDataChange.courier" placeholder="请选择快递员">
                    <el-option v-for="courier in courierList" :label="courier.name" :value="courier.id" :key="courier.id"></el-option>
                </el-select>
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
        name:"NotAssignedView",
        data(){
            return{
                socketServe: SocketService.Instance,
                keyword:null,
                list:[],
                formDataChange:{
                    id:"",
                    location:"",
                    addressee:"",
                    phone:"",
                    courier:""
                },
                dialogFormChangeVisible:false,
                courierList:[],
            }
        },
        mounted(){
            this.socketServe=SocketService.Instance;
            this.socketServe.send("SearchNotAssignedOrder:"+this.keyword);
            this.socketServe.send("SearchCourier:null");
            // 添加socket通知监听
	        window.addEventListener('onmessageWS', this.getSocketData)
        },
        methods:{
            getSocketData(res){
                console.log(res.detail.data);
                if(res.detail.data.includes("FUND_NOTASSGINED_ORDER")){
                    var msg=res.detail.data.split("&");
                    console.log(msg.length);
                    for(var i=1;i<msg.length;i++){
                        var m=msg[i].split(":");
                        var item={
                            "id":m[0],
                            "location":m[1],
                            "addressee":m[2],
                            "phone":m[3],
                        }
                        this.list.push(item);
                    }
                    console.table(this.list);
                }else if(res.detail.data.includes("NOTFUND_NOTASSGINED_ORDER")){
                    //未查找到订单
                }else if(res.detail.data=="UPDATE_ORDER_SUCCESS"){
                    this.$message({
                        message: '分配成功',
                        type: 'success'
                    });
                    this.dialogFormChangeVisible=false;
                    this.searchNotAssignedOrderByKeyword()
                }else if(res.detail.data.includes("FUND_COURIER")){
                    var msg=res.detail.data.split("&");
                    for(var i=1;i<msg.length;i++){
                        var m=msg[i].split(":");
                        var item={
                            "id":m[0],
                            "name":m[2],
                        }
                        this.courierList.push(item);
                    }
                    console.table(this.courierList);
                }
            },
            handleEdit(index,row){
                this.dialogFormChangeVisible=true;
                this.formDataChange.id=row.id;
                this.formDataChange.courier=row.courier;
            },
            changeConfirm(){
                this.socketServe.send("AssignCourier:"+this.formDataChange.id+":"+this.formDataChange.courier);
            },
            searchNotAssignedOrderByKeyword(){
                this.list=[];
                this.socketServe.send("SearchNotAssignedOrder:"+this.keyword);
            },
        },
    }
</script>

<style scoped>
#NotAssignedPage{
    padding: 20px;
}
.page-content{
    background-color: white;
    margin-top: 20px;
    display: flex;
    flex-direction: column;
}
</style>
