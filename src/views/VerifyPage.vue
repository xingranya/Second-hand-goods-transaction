<template>
  <section class="page-container verify-page">
    <article v-if="!authed" class="card form">
      <h2>学生实名认证</h2>
      <p class="hint">当前未登录，请先登录后再进行实名认证。</p>
      <RouterLink class="btn btn-primary" :to="loginLocation">去登录</RouterLink>
    </article>
    <form v-else class="card form" @submit.prevent="submit">
      <h2>学生实名认证</h2>
      <label>
        <span>姓名</span>
        <input v-model.trim="form.name" class="input" required />
      </label>
      <label>
        <span>学号</span>
        <input v-model.trim="form.studentNo" class="input" required />
      </label>
      <label>
        <span>学校</span>
        <input v-model.trim="form.school" class="input" required />
      </label>
      <div class="actions">
        <button class="btn btn-primary" type="submit">提交认证</button>
      </div>
      <p v-if="result" class="result">{{ result }}</p>
    </form>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from "vue";
import { storeToRefs } from "pinia";
import { RouterLink, useRoute } from "vue-router";
import { useUserStore } from "@/stores/user";
import { createLoginLocation } from "@/utils/auth";

const route = useRoute();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const result = ref("");
const authed = computed(() => isAuthenticated.value);
const loginLocation = computed(() => createLoginLocation(route));
const form = reactive({
  name: "",
  studentNo: "",
  school: ""
});

async function submit() {
  const resp = await userStore.submitVerifyForm(form);
  result.value = resp?.message || "提交成功";
}
</script>

<style scoped>
.verify-page {
  padding-top: 20px;
}

.form {
  width: min(620px, 100%);
  margin: 0 auto;
  padding: 16px;
  display: grid;
  gap: 10px;
}

h2 {
  margin: 0 0 4px;
}

label {
  display: grid;
  gap: 6px;
  color: var(--muted);
  font-size: 13px;
}

.actions {
  margin-top: 4px;
}

.result {
  margin: 0;
  color: var(--success);
}

.hint {
  margin: 0;
  color: var(--muted);
}
</style>
