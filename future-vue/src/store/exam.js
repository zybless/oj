import moment from 'moment'
import time from '@/common/time'
const state = {
    now: moment(),
    ifAuth: false // 是否验证成功，输入正确的密码后设置为true
  }
  const mutations = {
    changeIfAuth(state, payload) {
      state.ifAuth = payload.ifAuth;
    }
  }
  const actions ={
    setIfAuth ({commit}, payload) {
        commit('changeIfAuth', {
            ifAuth: payload.ifAuth
        })
      },
  }
  const getters =  {
    ifAuth: (state) => {
        return state.ifAuth
      },
  }
  export default {
    state,
    mutations,
    getters,
    actions
  }
  