# To use: configure I4J_KEY on env with the following:
# I4J_KEY=<GPG-4096-KEY>

VERSION="1.0.1"

# This may be commented if publish is to be run separately.
./gradlew clean
./gradlew publish

# SPI
cd inertia4j.spi/build/staging-deploy/io/github/inertia4j/inertia4j-spi/$VERSION
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spi-$VERSION.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spi-$VERSION-sources.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spi-$VERSION-javadoc.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spi-$VERSION.pom
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spi-$VERSION.module
cd "../../../../.."
zip -r ./inertia4j-spi-$VERSION.zip .
cd "../../.."

# CORE
cd inertia4j.core/build/staging-deploy/io/github/inertia4j/inertia4j-core/$VERSION
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-core-$VERSION.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-core-$VERSION-sources.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-core-$VERSION-javadoc.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-core-$VERSION.pom
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-core-$VERSION.module
cd "../../../../.."
zip -r ./inertia4j-core-$VERSION.zip .
cd "../../.."

# SPRING
cd inertia4j.spring/build/staging-deploy/io/github/inertia4j/inertia4j-spring/$VERSION
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spring-$VERSION.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spring-$VERSION-sources.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spring-$VERSION-javadoc.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spring-$VERSION.pom
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-spring-$VERSION.module
cd "../../../../.."
zip -r ./inertia4j-spring-$VERSION.zip .
cd "../../.."

# KTOR
cd inertia4j.ktor/build/staging-deploy/io/github/inertia4j/inertia4j-ktor/$VERSION
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-ktor-$VERSION.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-ktor-$VERSION-sources.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-ktor-$VERSION-javadoc.jar
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-ktor-$VERSION.pom
gpg --armor --detach-sign --local-user $I4J_KEY inertia4j-ktor-$VERSION.module
cd "../../../../.."
zip -r ./inertia4j-ktor-$VERSION.zip .
cd "../../.."