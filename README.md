# Spiders (蛛群)
Spiders的目标是成为最快的java爬虫框架。

## 简介
spiders是一款爬中爬框架，我的设想是每爬取一个网页，就把他这个网页所含有的url记录下来，然后一方面接着爬，一方面送给分析器分析，做持久化。

## 原理
### 爬虫原理

### 模块分析
- spiders-worker
> 负责网页爬取url

- spiders-analyzer
> 负责解析

- spiders-downloader
> 负责持久化爬虫数据

### 框架设计
