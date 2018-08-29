package com.adik993.mytorrent.config;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.io.Processors;
import de.flapdoodle.embed.process.io.Slf4jLevel;
import de.flapdoodle.embed.process.io.directories.FixedPath;
import de.flapdoodle.embed.process.io.progress.Slf4jProgressListener;
import de.flapdoodle.embed.process.store.ArtifactStoreBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
@ConditionalOnProperty(prefix = "spring.mongodb.embedded", name = "artifact.storage-path")
public class EmbededMongoConfig {

    @Bean
    public IRuntimeConfig embeddedMongoRuntimeConfig(
            @Value("${spring.mongodb.embedded.artifact.storage-path}") String artifactStoragePath) {
        Logger logger = LoggerFactory
                .getLogger(getClass().getPackage().getName() + ".EmbeddedMongo");
        ProcessOutput processOutput = new ProcessOutput(
                Processors.logTo(logger, Slf4jLevel.INFO),
                Processors.logTo(logger, Slf4jLevel.ERROR), Processors.named(
                "[console>]", Processors.logTo(logger, Slf4jLevel.DEBUG)));
        return new RuntimeConfigBuilder().defaultsWithLogger(Command.MongoD, logger)
                .processOutput(processOutput).artifactStore(getArtifactStore(logger, artifactStoragePath))
                .build();
    }

    private ArtifactStoreBuilder getArtifactStore(Logger logger, String artifactStoragePath) {
        return new ExtractedArtifactStoreBuilder().defaults(Command.MongoD)
                .extractDir(new FixedPath(Paths.get(artifactStoragePath, "extracted").toString()))
                .download(new DownloadConfigBuilder()
                        .defaultsForCommand(Command.MongoD)
                        .artifactStorePath(new FixedPath(artifactStoragePath))
                        .progressListener(new Slf4jProgressListener(logger)).build());
    }
}
