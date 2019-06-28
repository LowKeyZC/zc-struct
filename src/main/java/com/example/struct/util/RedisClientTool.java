package com.example.struct.util;

import com.example.struct.enums.RedisDomainEnum;
import redis.clients.jedis.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * redis 工具
 * <p>
 * 为了避免复杂性。规定redis应用均从该工具类起步。
 * 实现多redis实例和池模式
 * 暂不支持基于客户端的分布式模式（redis服务端负责redis分片实现）
 *
 * @author zhangyt
 */
public class RedisClientTool {

    /**
     * 个业务连接池存储器
     */
    private static final Map<String, ShardedJedisPool> JEDIS_POOL_MAP = new ConcurrentHashMap<>();
    /**
     * 加载配置
     */
    private static Properties prop = new Properties();

    static {
        try (InputStream inputStream = RedisClientTool.class.getClassLoader()
                .getResourceAsStream("redis/redis.txt")) {
            prop.load(inputStream);

            /*
              默认业务jedis
             */
            String defaultDomain = prop.getProperty("defaultDomain");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定业务的连接池
     *
     * @param domain 指定业务
     * @return ShardedJedisPool
     */
    private static ShardedJedisPool getJedisPool(String domain) {

        //存在直接返回
        ShardedJedisPool jedisPool = JEDIS_POOL_MAP.get(domain);
        if (jedisPool != null) {
            return jedisPool;
        }

        //否则创建
        synchronized (JEDIS_POOL_MAP) {
            jedisPool = JEDIS_POOL_MAP.get(domain);
            if (jedisPool != null) {
                return jedisPool;
            }

            //池特性
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPoolConfig.setTestWhileIdle(true);
            jedisPoolConfig.setMaxTotal(Integer.parseInt(prop.getProperty(domain + ".maxTotal")));
            jedisPoolConfig.setMaxIdle(Integer.parseInt(prop.getProperty(domain + ".maxIdle")));
            jedisPoolConfig.setMinIdle(Integer.parseInt(prop.getProperty(domain + ".minIdle")));

            //连接特性
            List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
            JedisShardInfo si = new JedisShardInfo(
                    prop.getProperty(domain + ".host"),
                    Integer.parseInt(prop.getProperty(domain + ".port")),
                    Integer.parseInt(prop.getProperty(domain + ".timeoutMill")));
            //判断是否设置了密码
            String password = prop.getProperty(domain + ".password");
            if (password != null) {
                si.setPassword(password);
            }
            shards.add(si);
            jedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);

            JEDIS_POOL_MAP.put(domain, jedisPool);

            return jedisPool;
        }

    }

    //=== 通用组 ===

    /**
     * 获取值
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static String get(String domain, String key) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.get(key);
        }
    }

    /**
     * set 值
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param value  值
     */
    public static String set(String domain, String key, String value) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.set(key, value);
        }
    }

    /**
     * 设置 值
     * <p>
     * 设置过期时间
     *
     * @param domain        业务redis名称
     * @param key           键
     * @param value         值
     * @param expireSeconds 到期时间
     */
    public static String setex(String domain, String key, String value, int expireSeconds) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.setex(key, expireSeconds, value);
        }
    }

    /**
     * 删除 值
     * <p>
     * 设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static Long del(String domain, String key) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.del(key);
        }
    }

    /**
     * 判断是否存在
     * <p>
     * 设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static boolean exists(String domain, String key) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.exists(key);
        }
    }

    /**
     * 设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static Long expire(String domain, String key, int expiredSeconds) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.expire(key, expiredSeconds);
        }
    }

    //====== hash 结构系列 =====

    /**
     * 获取值
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param field  域
     */
    public static String hget(String domain, String key, String field) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.hget(key, field);
        }
    }

    /**
     * 设置 值
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param value  值
     */
    public static Long hset(String domain, String key, String field, String value) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.hset(key, field, value);
        }
    }

    /**
     * 删除 值
     * <p>
     * 设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static Long hdel(String domain, String key, String field) {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            return jedis.hdel(key, field);
        }
    }

    //======  自动序列化方式  ==

    /**
     * 获取值
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static <T> T get(String domain, String key, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ShardedJedis jedis = getJedisPool(domain).getResource()) {
            ObjectInputStream objectInputStream = null;
            byte[] data = jedis.get(key.getBytes(StandardCharsets.UTF_8));
            if (data == null) {
                return null;
            }
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) objectInputStream.readObject();
        }
    }

    /**
     * 获取值
     * <p>
     * <pre>
     *     强制字符串序列化适配
     * </pre>
     *
     * @param domain 业务redis名称
     * @param key    键
     */
    public static <T> T getObject(String domain, String key, Class<T> clazz) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ObjectInputStream objectInputStream = null;
        try {
            byte[] data = jedis.get(key.getBytes("UTF-8"));
            if (data == null) {
                return null;
            }
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param value  值 必须是可序列化的
     */
    public static String set(String domain, String key, Serializable value) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.set(key.getBytes("UTF-8"), byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * <pre>
     *     强制字符串序列化适配
     * </pre>
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param value  值 必须是可序列化的
     */
    public static String setObject(String domain, String key, Serializable value) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.set(key.getBytes(StandardCharsets.UTF_8), byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain         业务redis名称
     * @param key            键
     * @param value          值 必须是可序列化的
     * @param expiredSeconds 到期时间
     */
    public static String setex(String domain, String key, Serializable value, int expiredSeconds) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.setex(key.getBytes("UTF-8"), expiredSeconds, byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * <pre>
     *     强制字符串序列化适配
     * </pre>
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain         业务redis名称
     * @param key            键
     * @param value          值 必须是可序列化的
     * @param expiredSeconds 到期时间
     */
    public static String setexObject(String domain, String key, Serializable value, int expiredSeconds) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.setex(key.getBytes("UTF-8"), expiredSeconds, byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取值
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param field  域
     */
    public static <T> T hget(String domain, String key, String field, Class<T> clazz) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ObjectInputStream objectInputStream = null;
        try {
            byte[] data = jedis.hget(key.getBytes("UTF-8"), field.getBytes("UTF-8"));
            if (data == null) {
                return null;
            }
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取值
     * <p>
     * <pre>
     *     强制字符串序列化适配
     * </pre>
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param field  域
     */
    public static <T> T hgetObject(String domain, String key, String field, Class<T> clazz) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ObjectInputStream objectInputStream = null;
        try {
            byte[] data = jedis.hget(key.getBytes("UTF-8"), field.getBytes("UTF-8"));
            if (data == null) {
                return null;
            }
            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param field  域
     * @param value  值 必须是可序列化的
     */
    public static Long hset(String domain, String key, String field, Serializable value) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.hset(key.getBytes("UTF-8"), field.getBytes("UTF-8"), byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * set 值
     * <p>
     * <pre>
     *     强制字符串序列化适配
     * </pre>
     * <p>
     * 注意没有设置过期时间
     *
     * @param domain 业务redis名称
     * @param key    键
     * @param field  域
     * @param value  值 必须是可序列化的
     */
    public static Long hsetObject(String domain, String key, String field, Serializable value) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            objectOutputStream.flush();
            return jedis.hset(key.getBytes("UTF-8"), field.getBytes("UTF-8"), byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 不支持的命令实现的接口
     */
    public static interface Command {
        public Object run(ShardedJedis jedis);
    }
    /**
     * 通用命令
     * <pre>此工具类不支持的其它快捷命令</pre>
     * @param domain 业务redis名称
     * @param command 实现命令接口的对象
     * @return Object
     */
    public static Object execute(String domain, Command command) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        try {
            return command.run(jedis);
        } finally {
            jedis.close();
        }
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String MILL_SECOND = "PX";
    private static final String DIS_LOCK = "DIS_LOCK";

    /**
     * 加分布式锁
     * @param domain
     * @param requestId
     * @return
     */
    public static boolean distributeLock(String domain,String requestId) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        try {
            String result = jedis.set(DIS_LOCK, requestId, SET_IF_NOT_EXIST, MILL_SECOND, 5000);
            return LOCK_SUCCESS.equals(result);
        } finally {
            jedis.close();
        }
    }

    /**
     * 解锁
     * @param domain
     * @param requestId
     */
    public static void releaseLock(String domain, String requestId) {
        ShardedJedis jedis = getJedisPool(domain).getResource();
        try {
            if (jedis.get(DIS_LOCK).equals(requestId)) {
                jedis.del(DIS_LOCK);
            }
        } finally {
            jedis.close();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(() -> System.out.println(Thread.currentThread().getName()+" "+distributeLock(RedisDomainEnum.MYDOMAIN.getName(),StringUtils.getRandomId())));
            thread.start();
            Thread.sleep(100);
        }
    }

}
