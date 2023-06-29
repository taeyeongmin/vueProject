<template>
  <div class="order">
    <div class="container">
      <main>
        <div class="py-5 text-center"><h2>주문하기</h2></div>
        <div class="row g-5">
          <div class="col-md-5 col-lg-4 order-md-last"><h4
              class="d-flex justify-content-between align-items-center mb-3"><span class="text-primary">구입 목록</span><span
              class="badge bg-primary rounded-pill">{{state.items.length}}</span></h4>

            <ul class="list-group mb-3">

              <li class="list-group-item d-flex justify-content-between lh-sm" v-for="(i, idx) in state.items" :key="idx">
                <div>
                  {{i.name}}
                </div>
                <span class="text-muted">{{lib.getNumberFormatted(i.price - i.price * i.discountPer / 100)}}</span>
              </li>
            </ul>
            <h3 class="text-center total-price">
              {{ lib.getNumberFormatted(totalPrice) }}원
            </h3>
          </div>
          <div class="col-md-7 col-lg-8"><h4 class="mb-3">주문자 정보</h4>
            <div class="needs-validation" novalidate="">
              <div class="row g-3">
                <div class="col-12"><label for="username" class="form-label">이름</label>
                  <input type="text"
                      class="form-control"
                      id="username"
                      v-model="state.form.name">
                    <div class="invalid-feedback"> Your username is required.</div>
                  </div>
                </div>
                <div class="col-12"><label for="address" class="form-label">주소</label>
                  <input type="text"
                         class="form-control"
                         id="address"
                         v-model="state.form.address">
                  <div class="invalid-feedback"> Please enter your shipping address.</div>
                </div>
              <hr class="my-4">
              <h4 class="mb-3">결제 수단</h4>
              <div class="my-3">
                <div class="form-check">
                  <input id="card" name="paymentMethod" type="radio" class="form-check-input"
                         checked="" v-model="state.form.payment" value="card"><label class="form-check-label" for="card">신용카드</label></div>
                <div class="form-check">
                  <input id="bank" name="paymentMethod" type="radio" class="form-check-input"
                         v-model="state.form.payment" value="bank"><label class="form-check-label" for="bank">무통장 입금</label></div>
              </div>
              <div class="row gy-3">
                <label for="cc-name" class="form-label">카드 번호</label>
                <input type="text"
                       class="form-control"
                       id="cc-name"
                       v-model="state.form.cardNumber">
              </div>
              <hr class="my-4">
              <button class="w-100 btn btn-primary btn-lg" type="button" v-on:click="submit()">결제하기</button>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import {computed, reactive} from "vue";
import axios from "axios";
import router from "@/scripts/router";
import lib from "@/scripts/lib";

export default {
  name: "Order",
  setup(){
    const state = reactive({
      items:[],
      form:{
        name:"",
        address:"",
        payment:"",
        cardNumber:"",
        items:""
      }
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

    const totalPrice = computed(()=>{

      let result = 0;

      state.items.forEach(i=>{
        result += i.price - (i.price * i.discountPer / 100);
      });

      return result;
    });

    const submit = () =>{

      const args = state.form;
      args.items = JSON.stringify(state.items);


      axios.post("/api/orders", args)
          .then(()=>{

            alert("주문 완료");

          }).catch(()=>{

      })
    }

    return {
      state,
      lib,
      itemLoad,
      totalPrice,
      submit
    };
  }
}
</script>

<style scoped>

</style>
