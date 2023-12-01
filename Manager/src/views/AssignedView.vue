<template>
    <div id="AssignedPage">
        <!-- 面包屑 -->
        <el-breadcrumb separator="/" style="font-size:20px;">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>已分配订单</el-breadcrumb-item>
        </el-breadcrumb>
    
        <div class="page-content">
            <div style="display:flex;">
                <!-- 搜索框 -->
                <div class="search" style="width: 200px;margin:10px;">
                    <el-input placeholder="请输入关键词搜索" v-model="keyword" class="input-with-select">
                        <template #append>
                            <el-button slot="append" @click="searchAssignedOrderByKeyword()">
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
                height="570"
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
                prop="courier"
                label="快递员">
                </el-table-column>
                
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button
                        type="primary"
                        @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                        type="primary"
                        @click="handleCode(scope.$index, scope.row)">查码</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog title="修改订单信息" v-model="dialogFormChangeVisible">
            <el-form :model="formDataChange">
                <el-form-item label="配送地址" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.location" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="快递员" :label-width="formLabelWidth">
                    <el-input v-model="formDataChange.courier" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormChangeVisible = false">取 消</el-button>
                <el-button type="primary" @click="changeConfirm()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="订单二维码" v-model="dialogCodeImgVisible">
            <el-form>
                <div style="display:flex;align-items: center;justify-content: center;">
                    <img :src="ImageUrl" alt="订单二维码">
                </div>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import SocketService from '../components/websocket'
    export default{
        name:"AssignedView",
        data(){
            return{
                socketServe: SocketService.Instance,
                keyword:null,
                list:[],
                ImageUrl:null,
                formDataChange:{
                    id:"",
                    location:"",
                    addressee:"",
                    courier:""
                },
                dialogFormChangeVisible:false,
                dialogCodeImgVisible:false,
            }
        },
        mounted(){
            this.socketServe=SocketService.Instance;
            this.socketServe.send("SearchAssignedOrder:"+this.keyword);
            // 添加socket通知监听
	        window.addEventListener('onmessageWS', this.getSocketData)
        },
        methods:{
            getSocketData(res){
                console.log(res.data);
                console.log(res.detail.data);
                if(res.detail.data instanceof Blob){
                    console.log("Blob类型");
                    // 将接收到的二进制数据转换成Blob对象
                    const imgBlob = new Blob([res.detail.data], { type: 'image/jpeg' });
                    // 将图片数据显示在img标签中
                    this.ImageUrl = URL.createObjectURL(imgBlob);
                }else{
                    if(res.detail.data.includes("FUND_ASSGINED_ORDER")){
                        var msg=res.detail.data.split("&");
                        console.log(msg.length);
                        for(var i=1;i<msg.length;i++){
                            var m=msg[i].split(":");
                            var item={
                                "id":m[0],
                                "location":m[1],
                                "addressee":m[2],
                                "courier":m[3],
                            }
                            this.list.push(item);
                        }
                        console.table(this.list);
                    }else if(res.detail.data.includes("NOTFUND_ASSGINED_ORDER")){
                        //未查找到订单
                    }else if(res.detail.data=="UPDATE_ORDER_SUCCESS"){
                        this.$message({
                            message: '修改成功',
                            type: 'success'
                        });
                        this.dialogFormChangeVisible=false;
                        this.searchAssignedOrderByKeyword()
                    }
                }
            },
            handleEdit(index,row){
                this.dialogFormChangeVisible=true;
                this.formDataChange.id=row.id;
                this.formDataChange.location=row.location;
                this.formDataChange.courier=row.courier;
            },
            handleCode(index,row){
                this.socketServe.send("GetImage:"+row.id);
                this.dialogCodeImgVisible=true;
            },
            changeConfirm(){
                this.socketServe.send("OrderChange:"+this.formDataChange.id+":"+this.formDataChange.location+":"+this.formDataChange.courier);
            },
            searchAssignedOrderByKeyword(){
                this.list=[];
                this.socketServe.send("SearchAssignedOrder:"+this.keyword);
            },
        },
    }
</script>
<style scoped>
#AssignedPage{
    padding: 20px;
}
.page-content{
    background-color: white;
    margin-top: 20px;
    display: flex;
    flex-direction: column;
}
</style>