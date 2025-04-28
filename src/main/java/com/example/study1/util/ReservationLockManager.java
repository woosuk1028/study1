package com.example.study1.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReservationLockManager {
    private final Map<Long, String> lockedPosts = new ConcurrentHashMap<>();

    public synchronized boolean tryLock(Long postId, String username) {
        if(lockedPosts.containsKey(postId)) {
            return false;
        }
        lockedPosts.put(postId, username);
        return true;
    }

    public synchronized void unlock(Long postId, String username) {
        if(lockedPosts.containsKey(postId) && lockedPosts.get(postId).equals(username)) {
            lockedPosts.remove(postId);
        }
    }

    public synchronized boolean isLocked(Long postId) {
        return lockedPosts.containsKey(postId);
    }
}
