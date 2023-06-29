<template>
  <div class="cart">
    <div class="container">
      <ul>
        <li v-for="(i,idx) in state.items" :key="idx">
          <img :src="i.imgPath"/>
          <span class="name">{{i.name}}</span>
          <span class="price">{{lib.getNumberFormatted(i.price - i.price * i.discountPer / 100 )}}원</span>
          <i class="fa fa-trash" @click="remove(i.id)"></i> <!-- 아이템의 id를 인자로 전달 -->
        </li>
      </ul>
      <router-link to="/order" class="btn btn-primary">구입하기</router-link>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import lib from "@/scripts/lib";

export default {
  name: "Cart",
  setup(){
    const state = reactive({
      items:[]
    })

    const itemLoad = () => {
      axios.get("/api/cart/items")
          .then((res)=>{
            console.log(res);
            console.log(res.data);

            state.items = res.data;
            console.log("state :",state.items)
          }).catch(()=>{
        alert("로그인 해주세요");
        router.push("/login");
      })
    }

    itemLoad();

    const remove = (itemId) =>{
      axios.delete(`/api/cart/items/${itemId}`)
          .then(()=>{
            console.log("삭제 완료");
            itemLoad();
          })
    }

    return {
      state,
      lib,
      itemLoad,
      remove
    };
  }
}
</script>

<style scoped>
  .cart ul{
    list-style: none;
    margin: 0;
    padding: 0;
  }
  .cart ul li{
    border: 1px solid #eee;
    margin-top: 25px;
    margin-bottom: 25px;
  }
  .cart ul li img{
    width: 150px;
    height: 150px;
  }

  .cart ul li .name, .cart ul li .price{
    margin-left: 25px;
  }

  .cart ul li i{
    float: right;
    font-size: 20px;
    margin-top: 65px;
    margin-right: 50px;
  }

  .cart .btn{
    display: block;
    margin:0 auto;
    padding: 30px 50px;
    font-size: 20px;
  }

</style>
