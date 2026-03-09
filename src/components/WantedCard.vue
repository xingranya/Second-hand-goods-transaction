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
    <p>{{ item.description }}</p>
    <footer>
      <div class="meta">
        <strong>{{ item.expectedPrice }}</strong>
        <span>{{ item.deadline }} · {{ item.campus }}</span>
      </div>
      <button class="btn btn-primary" @click="$emit('contact', item)">联系TA</button>
    </footer>
  </article>
</template>

<script setup>
defineProps({
  item: {
    type: Object,
    required: true
  }
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
</style>
