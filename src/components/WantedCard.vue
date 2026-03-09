<template>
  <article class="card wanted-card">
    <header>
      <span class="tag">
        <span class="material-symbols-outlined">campaign</span>
        求购
      </span>
      <small>{{ item.publishTime }}</small>
    </header>
    <h3>{{ item.title }}</h3>
    <p class="publisher">{{ publisherLabel }}</p>
    <p>{{ item.description }}</p>
    <footer>
      <div class="meta">
        <strong>{{ item.expectedPrice }}</strong>
        <span>{{ item.deadline }} · {{ item.campus }}</span>
      </div>
      <div class="action-group">
        <RouterLink class="btn btn-muted" :to="{ path: '/search', query: { keyword: item.title } }">
          去购买
        </RouterLink>
        <button class="btn btn-primary" @click="$emit('contact', item)">联系TA</button>
      </div>
    </footer>
  </article>
</template>

<script setup>
import { computed } from "vue";
import { RouterLink } from "vue-router";

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
});

const publisherLabel = computed(() => {
  const name = String(props.item?.publisher?.name || "").trim();
  if (!name) return "匿名同学";
  return name.endsWith("同学") ? name : `${name}同学`;
});

defineEmits(["contact"]);
</script>

<style scoped>
.wanted-card {
  padding: 16px;
}

header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

h3 {
  margin: 10px 0 6px;
}

.publisher {
  margin: 0 0 6px;
  color: var(--primary);
  font-size: 13px;
  font-weight: 600;
}

p {
  margin: 0;
  color: var(--muted);
  font-size: 14px;
  line-height: 1.5;
}

footer {
  margin-top: 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.meta strong {
  display: block;
  color: var(--accent);
}

.meta span {
  font-size: 12px;
  color: var(--muted);
}

.action-group {
  display: flex;
  gap: 8px;
}
</style>
