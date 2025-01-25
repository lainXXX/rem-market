package top.javarem.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.javarem.api.IDCCService;
import top.javarem.api.response.Response;
import top.javarem.types.enums.ResponseCode;

import java.nio.charset.StandardCharsets;

/**
 * @Author: rem
 * @Date: 2025/01/24/19:05
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/${app.config.api-version}/raffle/dcc")
@CrossOrigin("*")
@DubboService(version = "1.0")
public class DCCController implements IDCCService {

    private final CuratorFramework client;

    public DCCController(CuratorFramework client) {
        this.client = client;
    }

    private static final String BASE_CONFIG_PATH = "/rem-market-dcc";
    private static final String BASE_CONFIG_PATH_CONFIG = BASE_CONFIG_PATH + "/config";

    @GetMapping("/update_config")
    @Override
    public Response<Boolean> updateConfig(@RequestParam String key, @RequestParam String value) {
        try {
            log.info("DCC 动态配置值变更开始 key:{} value:{}", key, value);
            String keyPath = BASE_CONFIG_PATH_CONFIG.concat("/").concat(key);
            if (null == client.checkExists().forPath(keyPath)) {
                client.create().creatingParentsIfNeeded().forPath(keyPath);
                log.info("DCC 节点监听 base node {} not absent create new done!", keyPath);
            }
            Stat stat = client.setData().forPath(keyPath, value.getBytes(StandardCharsets.UTF_8));
            log.info("DCC 动态配置值变更完成 key:{} value:{} time:{}", key, value, stat.getCtime());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("DCC 动态配置值变更失败 key:{} value:{}", key, value, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
