import {createStore} from "vuex";

const store = createStore({
    state(){
        return{
            account:{
                id:0
            }
        }
    },
    mutations:{     // state값을 변경할 수 있는 유일한 방법 - store.commit('setAccount',0); 이런 형태로 사용
        setAccount(state, payload){
            state.account.id = payload;
        }
    }
})

export default store;
