<template>
  <section class="login-page">
    <div class="ambient ambient-left"></div>
    <div class="ambient ambient-right"></div>
    <section class="page-container login-shell">
      <article class="brand-panel">
        <p class="eyebrow">校园二手交易平台</p>
        <h1>让闲置物品在校园里更高效地流转。</h1>
        <p class="lead">
          提供商品发布、求购信息、即时沟通、订单跟进与实名认证等功能，帮助买卖双方更安心地完成交易。
        </p>

        <section class="highlights">
          <article class="highlight-card">
            <span class="material-symbols-outlined">storefront</span>
            <div>
              <strong>完整交易链路</strong>
              <p>从商品浏览、沟通确认到订单处理，关键交易环节衔接清晰。</p>
            </div>
          </article>
          <article class="highlight-card">
            <span class="material-symbols-outlined">verified_user</span>
            <div>
              <strong>校园实名机制</strong>
              <p>结合校区与身份信息，提升校园二手交易的可信度与安全感。</p>
            </div>
          </article>
          <article class="highlight-card">
            <span class="material-symbols-outlined">forum</span>
            <div>
              <strong>消息与订单联动</strong>
              <p>商品详情、消息沟通与订单页面互相关联，减少信息断层。</p>
            </div>
          </article>
        </section>

        <div class="process-strip">
          <span>浏览好物</span>
          <span>沟通确认</span>
          <span>下单交易</span>
          <span>评价闭环</span>
        </div>
      </article>

      <article class="card login-card">
        <div class="card-top">
          <span class="tag">账号登录</span>
          <h2>欢迎回来</h2>
          <p>登录后可继续浏览商品、发布闲置、发送消息和查看订单。</p>
          <p v-if="redirectTarget !== '/profile'" class="redirect-tip">
            登录后将自动返回 <strong>{{ redirectTarget }}</strong>
          </p>
        </div>

        <form class="form" @submit.prevent="submit">
          <label class="field">
            <span>用户名</span>
            <input
              v-model.trim="form.username"
              class="input"
              :class="{ 'input-error': Boolean(errorMessage) }"
              placeholder="请输入用户名"
              autocomplete="username"
              :disabled="submitting"
              required
            />
          </label>

          <label class="field">
            <span>密码</span>
            <div class="password-field">
              <input
                v-model.trim="form.password"
                class="input"
                :class="{ 'input-error': Boolean(errorMessage) }"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                autocomplete="current-password"
                :disabled="submitting"
                required
              />
              <button type="button" class="toggle-btn" :disabled="submitting" @click="showPassword = !showPassword">
                {{ showPassword ? "隐藏" : "显示" }}
              </button>
            </div>
          </label>

          <p v-if="errorMessage" class="error">
            <span class="material-symbols-outlined">error</span>
            {{ errorMessage }}
          </p>

          <div class="form-footer">
            <div class="safe-note">
              <span class="material-symbols-outlined">shield_lock</span>
              <span>登录成功后会自动同步账号信息与页面状态。</span>
            </div>
            <button class="btn btn-primary submit-btn" type="submit" :disabled="submitting">
              {{ submitting ? "登录中..." : "进入校园集市" }}
            </button>
          </div>
        </form>
      </article>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { storeToRefs } from "pinia";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
import { resolveRedirectTarget } from "@/utils/auth";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const { isAuthenticated } = storeToRefs(userStore);
const submitting = ref(false);
const showPassword = ref(false);
const errorMessage = ref("");
const form = reactive({
  username: "",
  password: ""
});

const redirectTarget = computed(() => resolveRedirectTarget(route.query.redirect));

async function goAfterLogin() {
  await router.replace(redirectTarget.value);
}

async function submit() {
  if (submitting.value) return;
  submitting.value = true;
  errorMessage.value = "";
  try {
    await userStore.login(form);
    await goAfterLogin();
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || "登录失败，请检查账号密码";
  } finally {
    submitting.value = false;
  }
}

onMounted(() => {
  if (isAuthenticated.value) {
    goAfterLogin();
  }
});
</script>

<style scoped>
.login-page {
  position: relative;
  min-height: calc(100vh - 64px);
  overflow: hidden;
  padding: 28px 0 44px;
}

.ambient {
  position: absolute;
  border-radius: 999px;
  filter: blur(18px);
  opacity: 0.8;
  pointer-events: none;
}

.ambient-left {
  width: 320px;
  height: 320px;
  left: -70px;
  top: 48px;
  background: radial-gradient(circle, rgba(100, 148, 255, 0.28) 0%, rgba(100, 148, 255, 0) 72%);
}

.ambient-right {
  width: 280px;
  height: 280px;
  right: -80px;
  bottom: 72px;
  background: radial-gradient(circle, rgba(255, 177, 107, 0.3) 0%, rgba(255, 177, 107, 0) 72%);
}

.login-shell {
  position: relative;
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(360px, 420px);
  gap: 24px;
  align-items: stretch;
}

.brand-panel {
  position: relative;
  padding: 34px 30px;
  border-radius: 30px;
  background:
    linear-gradient(160deg, rgba(255, 255, 255, 0.68), rgba(237, 244, 255, 0.88)),
    linear-gradient(135deg, rgba(36, 83, 199, 0.08), rgba(234, 109, 39, 0.08));
  border: 1px solid rgba(132, 159, 210, 0.2);
  box-shadow: 0 24px 60px rgba(31, 61, 126, 0.12);
  overflow: hidden;
}

.brand-panel::after {
  content: "";
  position: absolute;
  right: -44px;
  top: 28px;
  width: 170px;
  height: 170px;
  border-radius: 32px;
  background: linear-gradient(160deg, rgba(255, 255, 255, 0.88), rgba(207, 224, 255, 0.38));
  transform: rotate(18deg);
}

.eyebrow {
  margin: 0;
  letter-spacing: 0.24em;
  text-transform: uppercase;
  color: var(--primary);
  font-size: 12px;
  font-weight: 800;
}

h1 {
  position: relative;
  z-index: 1;
  margin: 16px 0 14px;
  max-width: 12em;
  font-family: var(--font-display);
  font-size: clamp(34px, 4vw, 52px);
  line-height: 1.2;
}

.lead {
  position: relative;
  z-index: 1;
  max-width: 38rem;
  margin: 0;
  color: var(--muted);
  line-height: 1.8;
  font-size: 15px;
}

.highlights {
  position: relative;
  z-index: 1;
  margin-top: 28px;
  display: grid;
  gap: 14px;
}

.highlight-card {
  display: grid;
  grid-template-columns: 44px 1fr;
  gap: 14px;
  align-items: start;
  padding: 16px 18px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.74);
  border: 1px solid rgba(132, 159, 210, 0.16);
}

.highlight-card .material-symbols-outlined {
  display: grid;
  place-items: center;
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: linear-gradient(135deg, rgba(36, 83, 199, 0.14), rgba(234, 109, 39, 0.14));
  color: var(--primary);
}

.highlight-card strong {
  display: block;
  margin-bottom: 6px;
  font-size: 16px;
}

.highlight-card p {
  margin: 0;
  color: var(--muted);
  line-height: 1.65;
}

.process-strip {
  position: relative;
  z-index: 1;
  margin-top: 26px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.process-strip span {
  padding: 9px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: var(--primary-strong);
  font-size: 13px;
  font-weight: 700;
}

.login-card {
  display: grid;
  gap: 18px;
  padding: 26px 24px 22px;
}

.card-top h2 {
  margin: 12px 0 8px;
  font-family: var(--font-display);
  font-size: 32px;
}

.card-top p {
  margin: 0;
  color: var(--muted);
  line-height: 1.7;
}

.redirect-tip {
  margin-top: 10px !important;
  padding: 10px 12px;
  border-radius: 14px;
  background: var(--surface-soft);
  color: var(--text) !important;
  font-size: 13px;
}

.form {
  display: grid;
  gap: 14px;
}

.field {
  display: grid;
  gap: 6px;
  color: var(--muted);
  font-size: 13px;
  font-weight: 600;
}

.password-field {
  position: relative;
}

.password-field .input {
  padding-right: 72px;
}

.toggle-btn {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  border: 0;
  background: transparent;
  color: var(--primary);
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
}

.error {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  padding: 12px 14px;
  border-radius: 14px;
  background: rgba(220, 38, 38, 0.08);
  color: var(--danger);
  font-size: 14px;
}

.form-footer {
  display: grid;
  gap: 14px;
}

.safe-note {
  display: flex;
  gap: 8px;
  align-items: flex-start;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.6;
}

.submit-btn {
  min-height: 48px;
}

@media (max-width: 980px) {
  .login-shell {
    grid-template-columns: 1fr;
  }

  .brand-panel {
    padding: 26px 20px;
  }

  h1 {
    max-width: none;
  }
}

@media (max-width: 640px) {
  .login-page {
    padding-top: 18px;
  }

  .login-card {
    padding: 20px 16px 18px;
  }

  .card-top h2 {
    font-size: 28px;
  }
}
</style>
