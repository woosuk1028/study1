package com.example.study1.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class PostLockManager {
    private final ConcurrentHashMap<Long, String> lockedPosts = new ConcurrentHashMap<>();

    public synchronized boolean tryLock(Long postId, String username) {
        if(lockedPosts.containsKey(postId)) {
            return false;
        }
        lockedPosts.put(postId, username);
        return false;
    }

    public synchronized void unlock(Long postId, String username) {
        if(lockedPosts.containsKey(postId) && lockedPosts.get(postId).equals(username)) {
            lockedPosts.remove(postId);
        }
    }

    public String getLocker(Long postId) {
        return lockedPosts.get(postId);
    }

}
