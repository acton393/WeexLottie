# coding: utf-8

Pod::Spec.new do |s|
  s.name         = "WXLottie"
  s.version      = "1.0"
  s.summary      = "Weex plugin for lottie"

  s.description  = <<-DESC
                  Weex plugin for lottie
                   DESC

  s.homepage     = "https://github.com/acton393/WeexLottie"
  s.license = {
    :type => 'Copyright',
    :text => <<-LICENSE
            copyright
    LICENSE
  }
  s.authors      = {
                     "acton393" =>"zhangxing610321@gmail.com"
                   }
  s.platform     = :ios
  s.ios.deployment_target = "8.0"

  s.user_target_xcconfig  = { 'FRAMEWORK_SEARCH_PATHS' => "'$(PODS_ROOT)/WeexPluginLoader'" }

  s.source       = { :path=>'./'}
  s.source_files  = "ios/Sources/*.{h,m,mm}"
  
  s.requires_arc = true
  s.dependency "WeexPluginLoader"
  s.dependency "WeexSDK"
  s.dependency "lottie-ios"
end
