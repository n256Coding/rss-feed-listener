package com.n256.rssfeedlistener.util;

import com.rometools.rome.feed.synd.SyndEntry;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This is a utility class which gives functionality to monitor integrity of the data.
 */
public final class IntegrityUtil {

    private IntegrityUtil() {
    }

    /**
     * Calculate the md5sum in hex format for given arguments
     *
     * @param title title of the entry
     * @param description description of the entry
     * @param publishedDate published date of the entry
     * @return
     */
    public static String calcMd5SumOf(String title, String description, LocalDateTime publishedDate) {
        String checksumString = String.format("%s|%s|%s", title, description, publishedDate);

        return DigestUtils.md5Hex(checksumString);
    }

    public static String calcMd5SumOf(SyndEntry entry) {
        String title = entry.getTitle();
        String description = entry.getDescription().getValue();
        LocalDateTime publishedDate = entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return calcMd5SumOf(title, description, publishedDate);
    }
}
