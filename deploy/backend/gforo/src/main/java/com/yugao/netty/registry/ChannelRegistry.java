package com.yugao.netty.registry;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChannelRegistry {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Channel>> userRoomChannelMap
            = new ConcurrentHashMap<>();


    public void register(String userId, String roomId, Channel channel) {

        userRoomChannelMap
                .computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .put(roomId, channel);
    }

    public Channel getChannel(String userId, String roomId) {
        ConcurrentHashMap<String, Channel> roomMap = userRoomChannelMap.get(userId);
        if (roomMap == null) return null;
        return roomMap.get(roomId);
    }


    public void remove(String userId, String roomId) {
        ConcurrentHashMap<String, Channel> roomMap = userRoomChannelMap.get(userId);
        if (roomMap != null) {
            Channel ch = getChannel(userId, roomId);
            roomMap.remove(roomId);
            if (ch != null && ch.isActive())
                ch.close();
            // 如果该用户没有房间了，清除外层 map（可选）
            if (roomMap.isEmpty()) {
                userRoomChannelMap.remove(userId, roomMap);
            }
        }
    }

    public void removeByChannel(Channel ch) {
        for (String userId : userRoomChannelMap.keySet()) {
            ConcurrentHashMap<String, Channel> roomMap = userRoomChannelMap.get(userId);
            if (roomMap != null) {
                roomMap.entrySet().removeIf(entry -> entry.getValue().equals(ch));
                if (roomMap.isEmpty()) {
                    userRoomChannelMap.remove(userId, roomMap);
                }
            }
        }
    }

    public List<Channel> getUserChannels(String userId) {
        ConcurrentHashMap<String, Channel> roomMap = userRoomChannelMap.get(userId);
        return roomMap != null ? new ArrayList<>(roomMap.values()) : Collections.emptyList();
    }

    public List<Channel> getAllChannels() {
        return userRoomChannelMap.values().stream()
                .flatMap(roomMap -> roomMap.values().stream())
                .collect(Collectors.toList());
    }
}
