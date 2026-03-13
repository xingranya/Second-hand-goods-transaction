<template>
  <section class="admin-login page-container">
    <section class="login-shell">
      <article class="intro-panel">
        <span class="tag">平台管理入口</span>
        <h1>管理平台数据与运行状态</h1>
        <p>
          后台用于查看用户、商品、订单等核心业务数据，并处理日常管理操作。
        </p>
        <ul class="intro-list">
          <li>查看平台概览与关键统计指标</li>
          <li>管理商品状态、用户状态与订单信息</li>
          <li>快速返回前台页面继续查看业务内容</li>
        </ul>
      </article>

      <article class="card login-card">
        <div class="login-top">
          <span class="tag">管理员登录</span>
          <h2>欢迎进入管理中心</h2>
          <p>请输入具有后台权限的账号信息。</p>
        </div>
        <form class="form" @submit.prevent="submit">
          <label>
            <span>账号</span>
            <input v-model.trim="form.username" class="input" placeholder="请输入管理员用户名" required />
          </label>
          <label>
            <span>密码</span>
            <input v-model.trim="form.password" class="input" type="password" placeholder="请输入密码" required />
          </label>
          <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
          <button class="btn btn-primary" type="submit" :disabled="submitting">
            {{ submitting ? "登录中..." : "进入管理中心" }}
          </button>
        </form>
      </article>
    </section>
  </section>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import { resolveRedirectTarget } from "@/utils/auth";

const route = useRoute();
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
    if (!userStore.isAdmin) {
      userStore.logout();
      errorMessage.value = "当前账号没有管理权限，请使用管理员账号登录";
      return;
    }
    await router.replace(resolveRedirectTarget(route.query.redirect, "/admin/dashboard"));
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "管理员登录失败";
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.admin-login {
  min-height: 100vh;
  display: grid;
  align-items: center;
}

.login-shell {
  width: 100%;
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(360px, 440px);
  gap: 24px;
  align-items: stretch;
}

.intro-panel {
  padding: 32px 30px;
  border-radius: 28px;
  background:
    linear-gradient(145deg, rgba(255, 255, 255, 0.86), rgba(237, 244, 255, 0.92)),
    linear-gradient(135deg, rgba(36, 83, 199, 0.08), rgba(234, 109, 39, 0.08));
  border: 1px solid var(--border);
  box-shadow: var(--shadow);
}

.intro-panel h1,
.login-top h2 {
  margin: 0;
  font-family: var(--font-display);
}

.intro-panel h1 {
  margin-top: 16px;
  font-size: clamp(32px, 4vw, 46px);
  line-height: 1.22;
}

.intro-panel p,
.login-top p {
  margin: 0;
  color: var(--muted);
  line-height: 1.7;
}

.intro-list {
  margin: 22px 0 0;
  padding-left: 18px;
  color: var(--muted);
  display: grid;
  gap: 10px;
  line-height: 1.7;
}

.login-card {
  padding: 28px;
  display: grid;
  gap: 18px;
}

.login-top {
  display: grid;
  gap: 8px;
}

.login-top h2 {
  font-size: 30px;
}

.form {
  display: grid;
  gap: 12px;
}

label {
  display: grid;
  gap: 6px;
  color: var(--muted);
  font-size: 13px;
}

.error {
  color: var(--danger);
}

@media (max-width: 980px) {
  .login-shell {
    grid-template-columns: 1fr;
  }
}
</style>
