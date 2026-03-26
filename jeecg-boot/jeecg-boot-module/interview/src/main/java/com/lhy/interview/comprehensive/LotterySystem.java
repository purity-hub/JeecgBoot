package com.lhy.interview.comprehensive;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 抽奖系统 - 基于区间加权随机的概率抽奖实现
 * 
 * 功能特性：
 * 1. 支持动态配置奖品及概率
 * 2. 自动构建概率区间
 * 3. 高效的区间查找算法（二分查找优化）
 * 4. 支持未中奖情况
 */
public class LotterySystem {
    
    // 精度单位（10000 对应万分之一的精度）
    private static final int PRECISION = 10000;

    /**
     * -- GETTER --
     *  获取所有奖品配置
     */
    // 奖品列表
    @Getter
    private List<Prize> prizes;
    
    // 随机数生成器
    private Random random;
    
    /**
     * 奖品内部类
     */
    @Setter
    @Getter
    public static class Prize {
        // Getter 和 Setter
        private Integer id;           // 奖品 ID
        private String name;          // 奖品名称
        private Double probability;   // 中奖概率（0-1 之间的小数）
        private Integer start;        // 区间起始位置
        private Integer end;          // 区间结束位置
        
        public Prize(Integer id, String name, Double probability) {
            this.id = id;
            this.name = name;
            this.probability = probability;
        }

        @Override
        public String toString() {
            return String.format("奖品{id=%d, name='%s', probability=%.4f, range=[%d-%d]}",
                    id, name, probability, start, end);
        }
    }
    
    /**
     * 抽奖结果类
     */
    @Getter
    public static class LotteryResult {
        private boolean win;        // 是否中奖
        private Prize prize;        // 中奖奖品（未中奖时为 null）
        private Integer randomNum;  // 生成的随机数
        
        public LotteryResult(boolean win, Prize prize, Integer randomNum) {
            this.win = win;
            this.prize = prize;
            this.randomNum = randomNum;
        }

        @Override
        public String toString() {
            if (win && prize != null) {
                return String.format("中奖了！获得：%s (随机数：%d)", prize.getName(), randomNum);
            } else {
                return String.format("未中奖 (随机数：%d)", randomNum);
            }
        }
    }
    
    /**
     * 构造函数 - 初始化默认奖品配置
     */
    public LotterySystem() {
        this.prizes = new ArrayList<>();
        this.random = new Random();
        initDefaultPrizes();
    }
    
    /**
     * 初始化默认奖品配置（题目给定的配置）
     */
    private void initDefaultPrizes() {
        prizes.add(new Prize(1, "奖品 1", 0.01));  // 1%
        prizes.add(new Prize(2, "奖品 2", 0.02));  // 2%
        prizes.add(new Prize(3, "奖品 3", 0.03));  // 3%
        buildPrizeRanges();
    }
    
    /**
     * 自定义奖品配置
     * @param prizes 奖品列表
     */
    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
        buildPrizeRanges();
    }
    
    /**
     * 添加单个奖品
     * @param id 奖品 ID
     * @param name 奖品名称
     * @param probability 中奖概率
     */
    public void addPrize(Integer id, String name, Double probability) {
        Prize prize = new Prize(id, name, probability);
        this.prizes.add(prize);
        buildPrizeRanges();
    }
    
    /**
     * 构建奖品概率区间
     * 将概率转换为 [1, 10000] 的整数区间
     */
    private void buildPrizeRanges() {
        int currentStart = 1;
        
        for (Prize prize : prizes) {
            // 计算奖品占用的区间大小
            int rangeSize = (int) Math.round(prize.getProbability() * PRECISION);
            int currentEnd = currentStart + rangeSize - 1;
            
            prize.setStart(currentStart);
            prize.setEnd(currentEnd);
            
            currentStart = currentEnd + 1;
        }
        
        // 打印区间信息（可选，用于调试）
        System.out.println("=== 奖品概率区间配置 ===");
        for (Prize prize : prizes) {
            System.out.println(prize);
        }
        System.out.println("未中奖区间：" + currentStart + " - " + PRECISION);
        System.out.println("========================\n");
    }
    
    /**
     * 执行抽奖（基础版本）
     * @return 奖品名称或"未中奖"
     */
    public String draw() {
        LotteryResult result = drawWithResult();
        return result.toString();
    }
    
    /**
     * 执行抽奖（返回详细信息）
     * @return 抽奖结果对象
     */
    public LotteryResult drawWithResult() {
        // 生成 1 到 10000 之间的随机数
        int randomNum = random.nextInt(PRECISION) + 1;
        
        // 使用二分查找优化区间匹配
        Prize winningPrize = binarySearchPrize(randomNum);
        
        return new LotteryResult(winningPrize != null, winningPrize, randomNum);
    }
    
    /**
     * 二分查找匹配的奖品区间
     * @param randomNum 随机数
     * @return 匹配的奖品，未匹配返回 null
     */
    private Prize binarySearchPrize(int randomNum) {
        int left = 0;
        int right = prizes.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Prize prize = prizes.get(mid);
            
            if (randomNum >= prize.getStart() && randomNum <= prize.getEnd()) {
                return prize;
            } else if (randomNum < prize.getStart()) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        // 未找到匹配的区间，表示未中奖
        return null;
    }
    
    /**
     * 线性查找匹配的奖品区间（简单版本）
     * @param randomNum 随机数
     * @return 匹配的奖品，未匹配返回 null
     */
    private Prize linearSearchPrize(int randomNum) {
        for (Prize prize : prizes) {
            if (randomNum >= prize.getStart() && randomNum <= prize.getEnd()) {
                return prize;
            }
        }
        return null;
    }
    
    /**
     * 批量测试抽奖结果
     * @param times 测试次数
     */
    public void testDraw(int times) {
        Map<String, Integer> statistics = new HashMap<>();
        
        for (int i = 0; i < times; i++) {
            LotteryResult result = drawWithResult();
            String prizeName = result.isWin() ? result.getPrize().getName() : "未中奖";
            statistics.put(prizeName, statistics.getOrDefault(prizeName, 0) + 1);
        }
        
        // 输出统计结果
        System.out.println("=== 抽奖测试结果（" + times + "次） ===");
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / times;
            System.out.printf("%-10s: %5d次，占比：%.2f%%\n", 
                    entry.getKey(), entry.getValue(), percentage);
        }
        System.out.println("================================\n");
    }

    /**
     * 主函数 - 测试示例
     */
    public static void main(String[] args) {
        System.out.println("🎉 欢迎使用抽奖系统 🎉\n");
        
        // 创建抽奖系统实例
        LotterySystem lotterySystem = new LotterySystem();
        
        // 单次抽奖测试
        System.out.println("【单次抽奖测试】");
        for (int i = 0; i < 5; i++) {
            System.out.println("第" + (i + 1) + "次：" + lotterySystem.draw());
        }
        
        // 批量抽奖测试（验证概率准确性）
        System.out.println("\n【批量抽奖测试 - 10000 次】");
        lotterySystem.testDraw(10000);
        
        // 自定义奖品配置测试
        System.out.println("【自定义奖品配置测试】");
        LotterySystem customLottery = new LotterySystem();
        List<Prize> customPrizes = new ArrayList<>();
        customPrizes.add(new Prize(101, "特等奖", 0.001));   // 0.1%
        customPrizes.add(new Prize(102, "一等奖", 0.01));    // 1%
        customPrizes.add(new Prize(103, "二等奖", 0.05));    // 5%
        customPrizes.add(new Prize(104, "三等奖", 0.10));    // 10%
        customPrizes.add(new Prize(105, "鼓励奖", 0.20));    // 20%
        customLottery.setPrizes(customPrizes);
        customLottery.testDraw(10000);
    }
}
