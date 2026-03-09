<template>
  <section class="page-container login-page">
    <form class="card form" @submit.prevent="submit">
      <h2>账号登录</h2>
      <label>
        <span>用户名</span>
        <input v-model.trim="form.username" class="input" required />
      </label>
      <label>
        <span>密码</span>
        <input v-model.trim="form.password" class="input" type="password" required />
      </label>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      <div class="actions">
        <button class="btn btn-primary" type="submit" :disabled="submitting">
          {{ submitting ? "登录中..." : "登录" }}
        </button>
      </div>
    </form>
  </section>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";

const router = useRouter();
const userStore = useUserStore();
const submitting = ref(false);
const errorMessage = ref("");
const form = reactive({
  username: "",
  password: ""
});

async function submit() {
  if (submitting.value) return;
  submitting.value = true;
  errorMessage.value = "";
  try {
    await userStore.login(form);
    await userStore.loadProfile();
    router.push("/profile");
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "登录失败，请检查账号密码";
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.login-page {
  padding-top: 24px;
}

.form {
  width: min(460px, 100%);
  margin: 0 auto;
  padding: 18px;
  display: grid;
  gap: 12px;
}

h2 {
  margin: 0;
}

label {
  display: grid;
  gap: 6px;
  color: var(--muted);
  font-size: 13px;
}

.actions {
  display: flex;
  justify-content: flex-end;
}

.error {
  margin: 0;
  color: #dc2626;
}
</style>
