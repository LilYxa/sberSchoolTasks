package ru.sberSchool.tasks.task15.service;

/**
 * Interface for a download service that manages downloading files concurrently with configurable
 * settings such as the number of threads and speed limit.
 *
 * @author Elland Ilia
 */
public interface DownloadService {

    /**
     * Starts downloading files from a specified list of links.
     *
     * @param linksFile  the path to the file containing a list of URLs to download from.
     * @param outputDir  the directory where the downloaded files will be saved.
     * @param threads    the number of concurrent threads to use for downloading.
     * @param speedLimit the speed limit for downloads, specified in bytes per second.
     *                   A value of 0 or negative disables the speed limit.
     */
    void startDownload(String linksFile, String outputDir, int threads, long speedLimit);
}
