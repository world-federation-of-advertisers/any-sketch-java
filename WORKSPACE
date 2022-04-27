workspace(name = "any_sketch_java")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "new_git_repository")

# Common-cpp
http_archive(
    name = "wfa_common_cpp",
#    sha256 = "3110be93990a449ac8f60b534319d7d3a08aa118908fecd7b571a5e08260e560",
    strip_prefix = "common-cpp-5f0582b17ffc60d21894aaf2a402518ebe307871",
    url = "https://github.com/world-federation-of-advertisers/common-cpp/archive/5f0582b17ffc60d21894aaf2a402518ebe307871.tar.gz",
)

load("@wfa_common_cpp//build:common_cpp_repositories.bzl", "common_cpp_repositories")

common_cpp_repositories()

load("@wfa_common_cpp//build:common_cpp_deps.bzl", "common_cpp_deps")

common_cpp_deps()

# gRPC
#http_archive(
#    name = "com_github_grpc_grpc",
#    sha256 = "2060769f2d4b0d3535ba594b2ab614d7f68a492f786ab94b4318788d45e3278a",
#    strip_prefix = "grpc-1.33.2",
#    urls = [
#        "https://github.com/grpc/grpc/archive/v1.33.2.tar.gz",
#    ],
#)

load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")

# Includes boringssl, com_google_absl, and other dependencies.
grpc_deps()

load("@com_github_grpc_grpc//bazel:grpc_extra_deps.bzl", "grpc_extra_deps")

# Loads transitive dependencies of GRPC.
grpc_extra_deps()

# Need to import platforms separately for @bazel_tools//tools/jdk:jni in rules_swig to select specific cpu
http_archive(
    name = "platforms",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/0.0.5/platforms-0.0.5.tar.gz",
        "https://github.com/bazelbuild/platforms/releases/download/0.0.5/platforms-0.0.5.tar.gz",
    ],
    sha256 = "379113459b0feaf6bfbb584a91874c065078aa673222846ac765f86661c27407",
)

# Support Maven sources
http_archive(
    name = "rules_jvm_external",
    sha256 = "62133c125bf4109dfd9d2af64830208356ce4ef8b165a6ef15bbff7460b35c3a",
    strip_prefix = "rules_jvm_external-3.0",
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/3.0.zip",
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

# Maven
maven_install(
    artifacts = [
        "com.google.guava:guava:29.0-jre",
        "com.google.truth.extensions:truth-liteproto-extension:1.0.1",
        "com.google.truth.extensions:truth-proto-extension:1.0.1",
        "com.google.truth:truth:1.0.1",
        "junit:junit:4.13",
    ],
    generate_compat_repositories = True,
    repositories = [
        "https://repo.maven.apache.org/maven2/",
    ],
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

load("//build/wfa:repositories.bzl", "wfa_repo_archive")

wfa_repo_archive(
    name = "wfa_rules_swig",
    commit = "653d1bdcec85a9373df69920f35961150cf4b1b6",
    repo = "rules_swig",
    sha256 = "34c15134d7293fc38df6ed254b55ee912c7479c396178b7f6499b7e5351aeeec",
)

wfa_repo_archive(
    name = "any_sketch",
#    version = "0.1.0",
    commit = "4f063af376f9f9d0cbbba21ab9492515c8ca155c",
    repo = "any-sketch",
    sha256 = None,
)
