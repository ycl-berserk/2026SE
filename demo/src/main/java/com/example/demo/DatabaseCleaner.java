package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库清理工具类
 * 用于开发环境重置数据库
 */
public class DatabaseCleaner {
    
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:54321/test";
        String username = "system";
        String password = "123456";
        
        try {
            // 加载 PostgreSQL 驱动
            Class.forName("org.postgresql.Driver");
            
            // 连接数据库并执行
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                
                // 关闭自动提交
                conn.setAutoCommit(false);
                
                try (Statement stmt = conn.createStatement()) {
                
                    System.out.println("开始清理数据库...");
                    
                    // 第一步：删除 public schema
                    System.out.println("正在删除 public schema...");
                    try {
                        stmt.execute("DROP SCHEMA IF EXISTS public CASCADE");
                        System.out.println("✓ public schema 已删除");
                        conn.commit();
                    } catch (Exception e) {
                        System.out.println("⚠ 删除 schema 时出错: " + e.getMessage());
                    }
                    
                    // 第二步：重新创建 schema
                    System.out.println("正在重新创建 public schema...");
                    try {
                        stmt.execute("CREATE SCHEMA public");
                        System.out.println("✓ public schema 已创建");
                        conn.commit();
                    } catch (Exception e) {
                        System.out.println("⚠ 创建 schema 时出错（可能已存在）: " + e.getMessage());
                    }
                    
                    // 第三步：授权
                    System.out.println("正在设置权限...");
                    try {
                        stmt.execute("GRANT ALL ON SCHEMA public TO system");
                        stmt.execute("GRANT ALL ON SCHEMA public TO PUBLIC");
                        System.out.println("✓ 权限已设置");
                        conn.commit();
                    } catch (Exception e) {
                        System.out.println("⚠ 设置权限时出错: " + e.getMessage());
                    }
                    
                    System.out.println("\n✅ 数据库清理完成！");
                }
            }
            
        } catch (Exception e) {
            System.err.println("清理数据库时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
