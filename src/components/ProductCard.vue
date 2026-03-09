<template>
  <article class="card product-card">
    <RouterLink class="cover-link" :to="`/product/${product.id}`">
      <img :src="product.images?.[0] || fallbackImage" :alt="product.title" />
      <span class="quality-tag">{{ product.condition }}</span>
    </RouterLink>
    <div class="content">
      <RouterLink class="title" :to="`/product/${product.id}`">{{ product.title }}</RouterLink>
      <div class="price">
        <strong>¥{{ product.price }}</strong>
        <span v-if="product.originPrice">¥{{ product.originPrice }}</span>
      </div>
      <div class="meta">
        <span>{{ product.campus }}</span>
        <span>{{ product.publishTime }}</span>
      </div>
    </div>
  </article>
</template>

<script setup>
import { RouterLink } from "vue-router";

defineProps({
  product: {
    type: Object,
    required: true
  }
});

const fallbackImage = "https://images.unsplash.com/photo-1521017432531-fbd92d768814?w=1200";
</script>

<style scoped>
.product-card {
  overflow: hidden;
}

.cover-link {
  display: block;
  position: relative;
  aspect-ratio: 4 / 3;
}

.cover-link img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.quality-tag {
  position: absolute;
  left: 10px;
  top: 10px;
  background: rgba(0, 0, 0, 0.58);
  color: #fff;
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 11px;
  font-weight: 700;
}

.content {
  padding: 14px;
}

.title {
  display: block;
  font-size: 15px;
  font-weight: 700;
  line-height: 1.4;
  min-height: 40px;
}

.price {
  margin-top: 10px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.price strong {
  color: var(--accent);
  font-size: 22px;
}

.price span {
  color: var(--muted);
  text-decoration: line-through;
  font-size: 12px;
}

.meta {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  color: var(--muted);
  font-size: 12px;
}
</style>
