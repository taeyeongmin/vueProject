<template>
  <div class="order">
    <div class="container">
      <table class="table table-bordered">
        <thead>
          <th>번호</th>
          <th>주문자명</th>
          <th>주소</th>
          <th>결제 수단</th>
          <th>구입 항목</th>
        </thead>
        <tbody>
          <tr v-for="(o, idx) in state.orders" :key="idx">
              <td>{{state.orders.length - idx}}</td>
              <td>{{o.name}}</td>
              <td>{{o.address}}</td>
              <td>{{o.payment}}</td>
              <td>
                <div v-for="(i, idx2) in o.items" :key="idx2">
                  {{i.name}}
                </div>
              </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {reactive} from "vue";
import lib from "@/scripts/lib";
import axios from "axios";

export default {
  name: "Orders",
  setup(){
    const state = reactive({
     orders:[]
    })

    axios.get("/api/orders")

        .then(({data})=>{

          state.orders = [];

          for(let d of data){
            if(d.items){
              d.items = JSON.parse(d.items);
            }
            state.orders.push(d)
          }

          console.log(data);
          state.orders = data;
        })

    return {
      state,
      lib
    };
  }
}
</script>

<style scoped>

</style>
