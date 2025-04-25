<script setup lang="ts">
// 定义工作经历数据
const workExperience = [
  {
    company: "阿里云",
    position: "前端开发工程师",
    type: "正式", // 新增字段，区分实习和正式工作
    projects: [
      "云控制台前端开发",
      "数据可视化平台搭建"
    ]
  },
  {
    company: "腾讯",
    position: "全栈开发工程师",
    type: "正式", // 新增字段，区分实习和正式工作
    projects: [
      "微信小程序开发",
      "后台管理系统重构"
    ]
  },
  {
    company: "字节跳动",
    position: "前端架构师",
    type: "实习", // 新增字段，区分实习和正式工作
    projects: [
      "抖音直播前端架构设计",
      "国际化项目前端优化"
    ]
  }
];
</script>

<template>
  <div class="work-experience-container">
    <h1>工作经历</h1>
    <div v-for="(work, index) in workExperience" :key="index" class="work-item" :class="work.type">
      <h2>{{ work.company }}</h2>
      <p><strong>岗位:</strong> {{ work.position }}</p>
      <p><strong>类型:</strong> {{ work.type }}</p> <!-- 新增类型显示 -->
      <p><strong>项目:</strong></p>
      <ul>
        <li v-for="(project, idx) in work.projects" :key="idx">{{ project }}</li>
      </ul>
    </div>
  </div>
</template>

<style scoped lang="less">
.work-experience-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: linear-gradient(135deg, #f9f9f9, #e0e0e0); // 渐变背景
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); // 更丰富的阴影效果
  transition: transform 0.3s ease, box-shadow 0.3s ease; // 增加动画效果

  &:hover {
    transform: translateY(-5px); // 悬停时上移效果
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3); // 悬停时阴影加深
  }

  h1 {
    text-align: center;
    color: #333;
    margin-bottom: 20px;
    font-size: 2rem;
    font-weight: bold;
  }

  .work-item {
    margin-bottom: 20px;
    padding: 15px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease, box-shadow 0.3s ease; // 增加动画效果

    &:hover {
      transform: translateY(-3px); // 悬停时上移效果
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 悬停时阴影加深
    }

    h2 {
      color: #555;
      margin-bottom: 10px;
      font-size: 1.5rem;
    }

    p {
      color: #666;
      margin-bottom: 5px;
    }

    ul {
      list-style-type: disc;
      padding-left: 20px;

      li {
        color: #777;
      }
    }

    &.实习 {
      border-left: 4px solid #ff6b6b; // 实习类型左侧边框颜色
    }

    &.正式 {
      border-left: 4px solid #4ecdc4; // 正式类型左侧边框颜色
    }
  }
}
</style>