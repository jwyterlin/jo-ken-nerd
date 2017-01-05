# Sometimes it's a README fix, or something like that - which isn't relevant for
# including in a project's CHANGELOG for example
declared_trivial = github.pr_title.include? "#trivial"
  
# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"
  
# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500
  
# Don't let testing shortcuts get into master by accident
fail("fdescribe left in tests") if `grep -r fdescribe specs/ `.length > 1
fail("fit left in tests") if `grep -r fit specs/ `.length > 1

xcov.report(
   scheme: 'Jkpls',
   workspace: './sender/iOS/Jkpls/Jkpls.xcworkspace',
   minimum_coverage_percentage: 90
)

slather.configure("./sender/iOS/Jkpls/Jkpls.xcodeproj", "Jkpls", options: {
  source_directory: './.slather.yml',
  workspace: './sender/iOS/Jkpls/Jkpls.xcworkspace',
  html: true,
  output_directory: './fastlane/slather_report',
  decimals: 2
  })

slather.notify_if_coverage_is_less_than(minimum_coverage: 80)
slather.notify_if_modified_file_is_less_than(minimum_coverage: 60)
slather.show_coverage
